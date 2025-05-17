package com.medrec.services;

import com.medrec.dtos.UsersLogInRequestDTO;
import com.medrec.dtos.UsersLogInResponseDTO;
import com.medrec.gateways.UsersGateway;
import com.medrec.grpc.auth.Auth;
import com.medrec.grpc.auth.AuthServiceGrpc;
import com.medrec.grpc.users.Users;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    private static AuthService instance;

    private final JwtService jwtService = JwtService.getInstance();
    private final UsersGateway usersGateway = UsersGateway.getInstance();

    private final String ADMIN_EMAIL = System.getenv("ADMIN_EMAIL");
    private final String ADMIN_PASSWORD = System.getenv("ADMIN_PASSWORD");

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    @Override
    public void registerPatient(Users.CreatePatientRequest request, StreamObserver<Auth.RegisterResponse> responseObserver) {
        this.logger.info("Called RPC Register Patient");

        Users.CreatePatientRequest requestWithHashedPass = Users.CreatePatientRequest.newBuilder()
            .setFirstName(request.getFirstName())
            .setLastName(request.getLastName())
            .setEmail(request.getEmail())
            .setPassword(BcryptService.hashPassword(request.getPassword()))
            .setPin(request.getPin())
            .setGpId(request.getGpId())
            .setIsHealthInsured(request.getIsHealthInsured())
            .build();

        try {
            Users.Patient savedPatient= usersGateway.registerPatient(requestWithHashedPass);
            String token = jwtService.generateToken(request.getEmail(), "patient");
            responseObserver.onNext(
                Auth.RegisterResponse.newBuilder()
                    .setToken(token)
                    .setRole("patient")
                    .build()
            );
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void registerDoctor(Users.CreateDoctorRequest request, StreamObserver<Auth.RegisterResponse> responseObserver) {
        this.logger.info("Called RPC Register Doctor");

        Users.CreateDoctorRequest requestWithHashedPass = Users.CreateDoctorRequest.newBuilder()
            .setFirstName(request.getFirstName())
            .setLastName(request.getLastName())
            .setEmail(request.getEmail())
            .setPassword(BcryptService.hashPassword(request.getPassword()))
            .setIsGp(request.getIsGp())
            .setSpecialtyId(request.getSpecialtyId())
            .build();

        try {
            Users.Doctor doctor = usersGateway.registerDoctor(requestWithHashedPass);
            String token = jwtService.generateToken(request.getEmail(), "doctor");
            responseObserver.onNext(
                Auth.RegisterResponse.newBuilder()
                    .setToken(token)
                    .setRole("doctor")
                    .build()
            );
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void logPatientIn(Auth.LoginRequest request,StreamObserver<Auth.LoginResponse> responseObserver ) {
        this.logger.info("Called RPC Log Patient In");

        String email = request.getEmail();
        String password = request.getPassword();

        UsersLogInRequestDTO requestDTO = new UsersLogInRequestDTO(email, password);

        try {
            UsersLogInResponseDTO responseDTO = usersGateway.getPatientByEmail(requestDTO);

            if(BcryptService.checkPassword(password, responseDTO.getPassword())) {
                String token = jwtService.generateToken(email, "patient");
                responseObserver.onNext(
                    Auth.LoginResponse.newBuilder()
                        .setToken(token)
                        .setRole("patient")
                        .build()
                );
                responseObserver.onCompleted();
            } else {
                this.logger.warning("Invalid credentials for patient: " + email);
                responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid credentials").asRuntimeException());
            }
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void logDoctorIn(Auth.LoginRequest request,StreamObserver<Auth.LoginResponse> responseObserver ) {
        this.logger.info("Called RPC Log Doctor In");

        String email = request.getEmail();
        String password = request.getPassword();

        UsersLogInRequestDTO requestDTO = new UsersLogInRequestDTO(email, password);

        try {
            UsersLogInResponseDTO responseDTO = usersGateway.getDoctorByEmail(requestDTO);

            if(BcryptService.checkPassword(password, responseDTO.getPassword())) {
                String token = jwtService.generateToken(email, "doctor");
                responseObserver.onNext(
                    Auth.LoginResponse.newBuilder()
                        .setToken(token)
                        .setRole("patient")
                        .build());
                responseObserver.onCompleted();
            } else {
                this.logger.warning("Invalid credentials for doctor: " + email);
                responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid credentials").asRuntimeException());
            }
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void logAdminIn(Auth.LoginRequest request,StreamObserver<Auth.LoginResponse> responseObserver ) {
        this.logger.info("Called RPC Log Admin In");

        String email = request.getEmail();
        String password = request.getPassword();

        if(email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            String token = jwtService.generateToken(email, "admin");
            responseObserver.onNext(
                Auth.LoginResponse.newBuilder()
                    .setToken(token)
                    .setRole("admin")
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid credentials").asRuntimeException());
        }

        responseObserver.onCompleted();
    }

    @Override
    public void authorizeRequest(Auth.AuthorizationRequest request, StreamObserver<Auth.AuthorizationResponse> responseObserver) {
        this.logger.info("Called RPC Authorize Request");

        String token = request.getToken();
        List<String> requiredRole = request.getRequiredRolesList();

        if(token.isBlank() || requiredRole.isEmpty() || requiredRole.contains(null) || requiredRole.contains("") || requiredRole.contains(" ")) {
            responseObserver.onError(Status.ABORTED.withDescription("Bad request").asRuntimeException());
            return;
        }

        boolean isAuthorized = jwtService.isUserAuthorized(token, requiredRole);

        if(isAuthorized) {
            responseObserver.onNext(
                Auth.AuthorizationResponse.newBuilder()
                    .setIsTokenAuthorized(true)
                    .build()
            );
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("Unauthorized").asRuntimeException());
        }
    }

    @Override
    public void validateToken(Auth.TokenRequest request, StreamObserver<Auth.ValidateTokeResponse> responseObserver) {
        this.logger.info("Called RPC Validate Token");

        String token = request.getToken();

        if (token.isBlank()) {
            responseObserver.onError(Status.ABORTED.withDescription("Bad request").asRuntimeException());
            return;
        }

        boolean isValid = this.jwtService.isTokenValid(token);

        if (isValid) {
            responseObserver.onNext(
                Auth.ValidateTokeResponse.newBuilder()
                    .setValid(true)
                    .build()
            );
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("Invalid token").asRuntimeException());
        }
    }
}
