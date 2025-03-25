package com.medrec.gateways;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.PatientServiceGrpc;
import com.medrec.grpc.users.SpecialtyServiceGrpc;
import com.medrec.grpc.users.Users;
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

    public Users.isSuccessfulResponse createDoctorSpecialtyId(Users.Doctor doctor, int specialtyId) {
        Users.DoctorSpecialtyId doctorWithSpecialtyId = Users.DoctorSpecialtyId.newBuilder()
            .setFirstName(doctor.getFirstName())
            .setLastName(doctor.getFirstName())
            .setEmail(doctor.getEmail())
            .setPassword(doctor.getPassword())
            .setSpecialtyId(specialtyId)
            .setIsGp(doctor.getIsGp())
            .build();

        return doctorService.createDoctorSpecialtyId(doctorWithSpecialtyId);
    }

    public Users.DoctorResponse getDoctorById(int id) {
        return doctorService.getDoctorById(Int32Value.of(id));
    }

    public Users.DoctorResponse getDoctorByEmail(String email) {
        return doctorService.getDoctorByEmail(StringValue.of(email));
    }

    public Users.DoctorList getAllDoctors() {
        return doctorService.getAllDoctors(Empty.getDefaultInstance());
    }

    public Users.isSuccessfulResponse updateDoctor(Users.Doctor doctor) {
        return doctorService.updateDoctor(doctor);
    }

    public Users.isSuccessfulResponse deleteDoctor(int id) {
        return doctorService.deleteDoctorById(Int32Value.of(id));
    }

    public Users.isSuccessfulResponse createPatientDoctorId(Users.Patient patient, int doctorId) {
        Users.PatientDoctorId patientWithDoctorId = Users.PatientDoctorId.newBuilder()
            .setFirstName(patient.getFirstName())
            .setLastName(patient.getLastName())
            .setEmail(patient.getEmail())
            .setPassword(patient.getPassword())
            .setGpId(doctorId)
            .setIsHealthInsured(patient.getIsHealthInsured())
            .build();

        return patientService.createPatientDoctorId(patientWithDoctorId);
    }

    public Users.PatientResponse getPatientById(int id) {
        return patientService.getPatientById(Int32Value.of(id));
    }

    public Users.PatientResponse getPatientByEmail(String email) {
        return patientService.getPatientByEmail(StringValue.of(email));
    }

    public Users.PatientList getAllPatients() {
        return patientService.getAllPatients(Empty.getDefaultInstance());
    }

    public Users.isSuccessfulResponse updatePatient(Users.Patient patient) {
        return patientService.updatePatient(patient);
    }

    public Users.isSuccessfulResponse deletePatient(int id) {
        return patientService.deletePatientById(Int32Value.of(id));
    }

    public Users.isSuccessfulResponse createSpecialty(Users.Specialty specialty) {
        return specialtyService.createSpecialty(specialty);
    }

    public Users.SpecialtyResponse getSpecialtyById(int id) {
        return specialtyService.getSpecialtyById(Int32Value.of(id));
    }

    public Users.SpecialtiesList getAllSpecialties() {
        return specialtyService.getAllSpecialties(Empty.getDefaultInstance());
    }

    public Users.isSuccessfulResponse updateSpecialty(Users.Specialty specialty) {
        return specialtyService.updateSpecialty(specialty);
    }

    public Users.isSuccessfulResponse deleteSpecialty(int id) {
        return specialtyService.deleteSpecialtyById(Int32Value.of(id));
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

