package com.medrec.gateways;

import com.medrec.dtos.AuthResponseDTO;
import com.medrec.grpc.auth.Auth;
import com.medrec.grpc.auth.AuthServiceGrpc;
import com.medrec.grpc.users.Users;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Repository
public class AuthGateway {
    private final Logger logger = Logger.getLogger(AuthGateway.class.getName());

    private final ManagedChannel channel;
    private final AuthServiceGrpc.AuthServiceBlockingStub authService;

    public AuthGateway() {
        int port = Integer.parseInt(System.getenv("AUTH_PORT"));
        String host = System.getenv("AUTH_HOST");
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        authService = AuthServiceGrpc.newBlockingStub(channel);
    }

    public AuthResponseDTO registerDoctor(
        String firstName,
        String lastName,
        String email,
        String password,
        boolean isGp,
        int specialtyId
    ) {
        Users.DoctorSpecialtyId doctor = Users.DoctorSpecialtyId.newBuilder()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email)
            .setPassword(password)
            .setIsGp(isGp)
            .setSpecialtyId(specialtyId)
            .build();

        Auth.RegisterResponse response = authService.registerDoctor(doctor);

        return new AuthResponseDTO(
            response.getIsSuccessful(),
            response.getToken(),
            response.getRole()
        );
    }

    public AuthResponseDTO registerPatient(
        String firstName,
        String lastName,
        String email,
        String password,
        String pin,
        int generalPractitionerId,
        boolean isHealthInsured
    ) {
        this.logger.info(password);
        Users.PatientDoctorId patient = Users.PatientDoctorId.newBuilder()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email)
            .setPassword(password)
            .setPin(pin)
            .setGpId(generalPractitionerId)
            .setIsHealthInsured(isHealthInsured)
            .build();

        Auth.RegisterResponse response = authService.registerPatient(patient);

        return new AuthResponseDTO(
            response.getIsSuccessful(),
            response.getToken(),
            response.getRole()
        );
    }

    public String logPatientIn(String email, String password) {
        Auth.LoginResponse response = authService.logPatientIn(
            Auth.LoginRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        );

        if (response.getIsSuccessful()) {
            return response.getToken();
        }
        return null;
    }

    public String logDoctorIn(String email, String password) {
        Auth.LoginResponse response = authService.logDoctorIn(
            Auth.LoginRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        );

        if (response.getIsSuccessful()) {
            return response.getToken();
        }
        return null;
    }

    public boolean isRequestAuthorized(String token, String requiredRole) {
        Auth.AuthorizationRequest request = Auth.AuthorizationRequest.newBuilder()
            .setToken(token)
            .setRequiredRole(requiredRole)
            .build();

        Auth.AuthorizationResponse response = authService.authorizeRequest(request);

        return response.getIsTokenAuthorized();
    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down channel");
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            channel.shutdownNow();
        }
    }
}
