package com.medrec.gateways;

import com.google.protobuf.StringValue;
import com.medrec.dtos.UsersLogInRequestDTO;
import com.medrec.dtos.UsersLogInResponseDTO;
import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.PatientServiceGrpc;
import com.medrec.grpc.users.Users;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

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

    public Users.Doctor registerDoctor(Users.CreateDoctorRequest doctor) throws StatusRuntimeException {
        try {
            return doctorService.createDoctor(doctor);
        } catch (StatusRuntimeException e) {
            this.logger.info("Could not register doctor with email " + doctor.getEmail());
            throw e;
        }
    }

    public Users.Patient registerPatient(Users.CreatePatientRequest patient) throws StatusRuntimeException {
        try {
            return patientService.createPatient(patient);
        } catch (StatusRuntimeException e) {
            this.logger.info("Could not register patient with email " + patient.getEmail());
            throw e;
        }
    }

    public UsersLogInResponseDTO getPatientByEmail(UsersLogInRequestDTO request) throws StatusRuntimeException {
        try {
            Users.Patient patient = patientService.getPatientByEmail(StringValue.of(request.getEmail()));
            return new UsersLogInResponseDTO(
                patient.getEmail(),
                patient.getPassword()
            );
        } catch (StatusRuntimeException e) {
            this.logger.info("Could not find patient with email" + request.getEmail());
            throw e;
        }
    }

    public UsersLogInResponseDTO getDoctorByEmail(UsersLogInRequestDTO request) throws StatusRuntimeException {
        try {
            Users.Doctor response = doctorService.getDoctorByEmail(StringValue.of(request.getEmail()));
            return new UsersLogInResponseDTO(
                response.getEmail(),
                response.getPassword()
            );
        } catch (StatusRuntimeException e) {
            this.logger.info("Could not find doctor with email " + request.getEmail());
            throw e;
        }
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
