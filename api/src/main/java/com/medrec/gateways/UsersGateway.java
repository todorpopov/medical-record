package com.medrec.gateways;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.medrec.exception_handling.ExceptionsMapper;
import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.PatientServiceGrpc;
import com.medrec.grpc.users.SpecialtyServiceGrpc;
import com.medrec.grpc.users.Users;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
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

    public Users.Doctor createDoctor(Users.CreateDoctorRequest doctor) {
        try {
            return doctorService.createDoctor(doctor);
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Users.Doctor getDoctorById(int id) {
        try {
            return doctorService.getDoctorById(Int32Value.of(id));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Users.Doctor getDoctorByEmail(String email) {
        try {
            return doctorService.getDoctorByEmail(StringValue.of(email));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Users.DoctorList getAllDoctors() {
        try {
            return doctorService.getAllDoctors(Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Users.DoctorList getAllGpDoctors() {
        try {
            return doctorService.getAllGPDoctors(Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Users.Doctor updateDoctor(Users.UpdateDoctorRequest doctor) {
        try {
            return doctorService.updateDoctor(doctor);
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public void deleteDoctor(int id) {
        try {
            Empty result = doctorService.deleteDoctorById(Int32Value.of(id));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Users.Patient createPatient(Users.CreatePatientRequest request) {
        try {
            return patientService.createPatient(request);
        } catch (StatusRuntimeException e) {
            throw
        }
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

    public Users.SpecialtiesList getAllSpecialties() throws StatusRuntimeException {
        try {
            return specialtyService.getAllSpecialties(Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
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

