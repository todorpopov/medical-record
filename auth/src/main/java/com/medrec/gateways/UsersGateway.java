package com.medrec.gateways;

import com.google.protobuf.StringValue;
import com.medrec.dtos.UsersLogInRequestDTO;
import com.medrec.dtos.UsersLogInResponseDTO;
import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.PatientServiceGrpc;
import com.medrec.grpc.users.Users;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UsersGateway {
    private static UsersGateway instance;

    private final Logger logger = Logger.getLogger(UsersGateway.class.getName());
    private final ManagedChannel channel;

    private final DoctorServiceGrpc.DoctorServiceBlockingStub doctorService;
    private final PatientServiceGrpc.PatientServiceBlockingStub patientService;

    private UsersGateway() {
        int port = Integer.parseInt(System.getenv("USERS_PORT"));
        String host = System.getenv("USERS_HOST");
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        doctorService = DoctorServiceGrpc.newBlockingStub(channel);
        patientService = PatientServiceGrpc.newBlockingStub(channel);
    }

    public static UsersGateway getInstance() {
        if (instance == null) {
            instance = new UsersGateway();
        }
        return instance;
    }

    public Users.isSuccessfulResponse registerDoctor(Users.DoctorSpecialtyId doctor) {
        return doctorService.createDoctorSpecialtyId(doctor);
    }

    public Users.isSuccessfulResponse registerPatient(Users.PatientDoctorId patient) {
        return patientService.createPatientDoctorId(patient);
    }

    public UsersLogInResponseDTO getPatientByEmail(UsersLogInRequestDTO request) {
        Users.PatientResponse response = patientService.getPatientByEmail(StringValue.of(request.getEmail()));
        if (response.getExists()) {
            return new UsersLogInResponseDTO(
                true,
                response.getPatient().getEmail(),
                response.getPatient().getPassword()
            );
        }
        return new UsersLogInResponseDTO(false);
    }

    public UsersLogInResponseDTO getDoctorByEmail(UsersLogInRequestDTO request) {
        Users.DoctorResponse response = doctorService.getDoctorByEmail(StringValue.of(request.getEmail()));
        if (response.getExists()) {
            return new UsersLogInResponseDTO(
                true,
                response.getDoctor().getEmail(),
                response.getDoctor().getPassword()
            );
        }
        return new UsersLogInResponseDTO(false);
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
