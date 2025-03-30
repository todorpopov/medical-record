package com.medrec.services;

import com.medrec.dtos.UsersLogInRequestDTO;
import com.medrec.dtos.UsersLogInResponseDTO;
import com.medrec.gateways.UsersGateway;
import com.medrec.grpc.auth.Auth;
import com.medrec.grpc.auth.AuthServiceGrpc;
import com.medrec.grpc.users.Users;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    private static AuthService instance;

    private final JwtService jwtService = JwtService.getInstance();
    private final UsersGateway usersGateway = UsersGateway.getInstance();

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    @Override
    public void registerPatient(Users.PatientDoctorId request, StreamObserver<Auth.RegisterResponse> responseObserver) {
        this.logger.info("Called RPC Register Patient");
        this.logger.info(request.getPassword());

        Users.PatientDoctorId requestWithHashedPass = Users.PatientDoctorId.newBuilder()
            .setFirstName(request.getFirstName())
            .setLastName(request.getLastName())
            .setEmail(request.getEmail())
            .setPassword(BcryptService.hashPassword(request.getPassword()))
            .setPin(request.getPin())
            .setGpId(request.getGpId())
            .setIsHealthInsured(request.getIsHealthInsured())
            .build();

        Users.isSuccessfulResponse response = usersGateway.registerPatient(requestWithHashedPass);

        if(response.getIsSuccessful()) {
            String token = jwtService.generateToken(request.getEmail(), "patient");
            responseObserver.onNext(
                Auth.RegisterResponse.newBuilder()
                    .setIsSuccessful(true)
                    .setToken(token)
                    .setRole("patient")
                    .build()
            );
        } else {
            responseObserver.onNext(
                Auth.RegisterResponse.newBuilder()
                    .setIsSuccessful(false)
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void registerDoctor(Users.DoctorSpecialtyId request, StreamObserver<Auth.RegisterResponse> responseObserver) {
        this.logger.info("Called RPC Register Doctor");

        Users.DoctorSpecialtyId requestWithHashedPass = Users.DoctorSpecialtyId.newBuilder()
            .setFirstName(request.getFirstName())
            .setLastName(request.getLastName())
            .setEmail(request.getEmail())
            .setPassword(BcryptService.hashPassword(request.getPassword()))
            .setIsGp(request.getIsGp())
            .setSpecialtyId(request.getSpecialtyId())
            .build();

        Users.isSuccessfulResponse response = usersGateway.registerDoctor(requestWithHashedPass);

        if(response.getIsSuccessful()) {
            String token = jwtService.generateToken(request.getEmail(), "doctor");
            responseObserver.onNext(
                Auth.RegisterResponse.newBuilder()
                    .setIsSuccessful(true)
                    .setToken(token)
                    .setRole("doctor")
                    .build()
            );
        } else {
            responseObserver.onNext(
                Auth.RegisterResponse.newBuilder()
                    .setIsSuccessful(false)
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void logPatientIn(Auth.LoginRequest request,StreamObserver<Auth.LoginResponse> responseObserver ) {
        this.logger.info("Called RPC Log Patient In");

        String email = request.getEmail();
        String password = request.getPassword();

        UsersLogInRequestDTO requestDTO = new UsersLogInRequestDTO(email, password);
        UsersLogInResponseDTO responseDTO = usersGateway.getPatientByEmail(requestDTO);

        if(responseDTO.getExists() && BcryptService.checkPassword(password, responseDTO.getPassword())) {
            String token = jwtService.generateToken(email, "patient");
            responseObserver.onNext(
                Auth.LoginResponse.newBuilder()
                    .setIsSuccessful(true)
                    .setToken(token)
                    .setRole("patient")
                    .build());
            responseObserver.onCompleted();
        } else {
        responseObserver.onNext(
            Auth.LoginResponse.newBuilder()
                .setIsSuccessful(false)
                .build());
        responseObserver.onCompleted();
        }
    }

    @Override
    public void logDoctorIn(Auth.LoginRequest request,StreamObserver<Auth.LoginResponse> responseObserver ) {
        this.logger.info("Called RPC Log Doctor In");

        String email = request.getEmail();
        String password = request.getPassword();

        UsersLogInRequestDTO requestDTO = new UsersLogInRequestDTO(email, password);
        UsersLogInResponseDTO responseDTO = usersGateway.getDoctorByEmail(requestDTO);

        if(responseDTO.getExists() && BcryptService.checkPassword(password, responseDTO.getPassword())) {
            String token = jwtService.generateToken(email, "doctor");
            responseObserver.onNext(
                Auth.LoginResponse.newBuilder()
                    .setIsSuccessful(true)
                    .setToken(token)
                    .setRole("patient")
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(
                Auth.LoginResponse.newBuilder()
                    .setIsSuccessful(false)
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void authorizeRequest(Auth.AuthorizationRequest request, StreamObserver<Auth.AuthorizationResponse> responseObserver) {
        this.logger.info("Called RPC Authorize Request");

        String token = request.getToken();
        String requiredRole = request.getRequiredRole();

        boolean isAuthorized = jwtService.isUserAuthorized(token, requiredRole);

        if(isAuthorized) {
            responseObserver.onNext(
                Auth.AuthorizationResponse.newBuilder()
                    .setIsTokenAuthorized(true)
                    .build()
            );
        } else {
            responseObserver.onNext(
                Auth.AuthorizationResponse.newBuilder()
                    .setIsTokenAuthorized(false)
                    .build()
            );
        }

        responseObserver.onCompleted();
    }
}
