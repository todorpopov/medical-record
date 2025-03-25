package com.medrec.gateways;

import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.PatientServiceGrpc;
import com.medrec.grpc.users.SpecialtyServiceGrpc;
import com.medrec.grpc.users.Users;
import com.medrec.utils.ResponseMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Repository
public class UsersGateway {
    private final Logger logger = Logger.getLogger(UsersGateway.class.getName());

    private final ManagedChannel channel;

    private final DoctorServiceGrpc.DoctorServiceBlockingStub doctorService;
    private final PatientServiceGrpc.PatientServiceBlockingStub patientService;
    private final SpecialtyServiceGrpc.SpecialtyServiceBlockingStub specialtyService;

    public UsersGateway() {
        int port = Integer.parseInt(System.getenv("USERS_PORT"));
        String host = System.getenv("USERS_HOST");

        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        doctorService = DoctorServiceGrpc.newBlockingStub(channel);
        patientService = PatientServiceGrpc.newBlockingStub(channel);
        specialtyService = SpecialtyServiceGrpc.newBlockingStub(channel);
    }

    public ResponseMessage createDoctorSpecialtyId(Users.Doctor doctor, int specialtyId) {
        Users.DoctorSpecialtyId doctorWithSpecialtyId = Users.DoctorSpecialtyId.newBuilder()
            .setFirstName(doctor.getFirstName())
            .setLastName(doctor.getFirstName())
            .setEmail(doctor.getEmail())
            .setPassword(doctor.getPassword())
            .setSpecialtyId(specialtyId)
            .setIsGp(doctor.getIsGp())
            .build();

        Users.isSuccessfulResponse response = doctorService.createDoctorSpecialtyId(doctorWithSpecialtyId);
        return new ResponseMessage(response.getIsSuccessful(), response.getMessage());
    }

    public ResponseMessage createPatientDoctorId(Users.Patient patient, int doctorId) {
        Users.PatientDoctorId patientWithDoctorId = Users.PatientDoctorId.newBuilder()
            .setFirstName(patient.getFirstName())
            .setLastName(patient.getLastName())
            .setEmail(patient.getEmail())
            .setPassword(patient.getPassword())
            .setGpId(doctorId)
            .setIsHealthInsured(patient.getIsHealthInsured())
            .build();

        Users.isSuccessfulResponse response = patientService.createPatientDoctorId(patientWithDoctorId);
        return new ResponseMessage(response.getIsSuccessful(), response.getMessage());
    }

    public ResponseMessage createSpecialty(Users.Specialty specialty) {
        Users.isSuccessfulResponse response = specialtyService.createSpecialty(specialty);
        return new ResponseMessage(response.getIsSuccessful(), response.getMessage());
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

