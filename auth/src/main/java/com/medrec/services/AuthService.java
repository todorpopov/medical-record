package com.medrec.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.medrec.dtos.UsersLogInRequestDTO;
import com.medrec.dtos.UsersLogInResponseDTO;
import com.medrec.gateways.UsersGateway;
import com.medrec.grpc.auth.Auth;
import com.medrec.grpc.auth.AuthServiceGrpc;
import com.medrec.grpc.users.Users;
import com.medrec.jwt.JwtUtil;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    private static AuthService instance;

    private final JwtUtil jwtUtil = JwtUtil.getInstance();
    private final UsersGateway usersGateway = UsersGateway.getInstance();

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    @Override
    public void registerPatient(Users.Patient request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {}

    @Override
    public void registerDoctor(Users.Doctor request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {}

    @Override
    public void logPatientIn(Auth.LoginRequest request,StreamObserver<Auth.LoginResponse> responseObserver ) {
        this.logger.info("Called RPC Log Patient In");

        String email = request.getEmail();
        String password = request.getPassword();

        UsersLogInRequestDTO requestDTO = new UsersLogInRequestDTO(email, password);
        UsersLogInResponseDTO responseDTO = usersGateway.getPatientByEmail(requestDTO);

        if(responseDTO.getExists() && responseDTO.getPassword().equals(password)) {
            String token = jwtUtil.generateToken(email, "patient");
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

        if(responseDTO.getExists() && responseDTO.getPassword().equals(password)) {
            String token = jwtUtil.generateToken(email, "patient");
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

        DecodedJWT jwt = jwtUtil.verifyAndDecode(token);
        if (jwt != null && requiredRole.equals(jwtUtil.getRole(jwt))) {
            responseObserver.onNext(
                Auth.AuthorizationResponse.newBuilder()
                    .setIsTokenAuthorized(true)
                    .build()
            );
            responseObserver.onCompleted();
        }

        responseObserver.onNext(
            Auth.AuthorizationResponse.newBuilder()
                .setIsTokenAuthorized(false)
                .build()
        );
        responseObserver.onCompleted();
    }
}
