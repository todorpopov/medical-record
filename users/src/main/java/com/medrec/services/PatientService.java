package com.medrec.services;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.medrec.dtos.CreatePatientDoctorIdDTO;
import com.medrec.exception_handling.ExceptionsMapper;
import com.medrec.exception_handling.exceptions.DoctorIsNotGpException;
import com.medrec.grpc.users.PatientServiceGrpc;
import com.medrec.grpc.users.Users;
import com.medrec.persistence.ResponseMessage;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.patient.Patient;
import com.medrec.persistence.patient.PatientRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;

public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {
    private static PatientService instance;

    private final Logger logger = Logger.getLogger(PatientService.class.getName());

    private final PatientRepository patientRepository = PatientRepository.getInstance();

    private PatientService() {
    }

    public static PatientService getInstance() {
        if (instance == null) {
            instance = new PatientService();
        }
        return instance;
    }

    @Override
    public void createPatient(Users.Patient request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Patient");

        try {
            Patient patient = getEntityFromRequest(request);
            ResponseMessage message = patientRepository.save(patient);

            responseObserver.onNext(
                Users.isSuccessfulResponse.newBuilder()
                    .setIsSuccessful(message.isSuccessful())
                    .setMessage(message.getMessage())
                    .build()
            );
        } catch (DoctorIsNotGpException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("doctor_not_gp").asRuntimeException());
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createPatientDoctorId(Users.PatientDoctorId request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Patient - Doctor Id");
        CreatePatientDoctorIdDTO dto = new CreatePatientDoctorIdDTO(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPassword(),
            request.getPin(),
            request.getGpId(),
            request.getIsHealthInsured()
        );

        try {
            ResponseMessage message = patientRepository.saveWithDoctorId(dto);
            responseObserver.onNext(
                Users.isSuccessfulResponse.newBuilder()
                    .setIsSuccessful(message.isSuccessful())
                    .setMessage(message.getMessage())
                    .build()
            );
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getPatientById(Int32Value request, StreamObserver<Users.PatientResponse> responseObserver) {
        this.logger.info("Called RPC Get Patient By Id");

        int id = request.getValue();
        try {
            Patient patient = patientRepository.findById(id);

            Users.PatientResponse patientResponse = Users.PatientResponse.newBuilder()
                .setPatient(getGrpcPatientFromEntity(patient))
                .setExists(true)
                .build();
            responseObserver.onNext(patientResponse);
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getPatientByEmail(StringValue request, StreamObserver<Users.PatientResponse> responseObserver) {
        this.logger.info("Called RPC Get Patient By Email");

        String email = request.getValue();
        try {
            Patient patient = patientRepository.findByEmail(email);
            Users.PatientResponse patientResponse = Users.PatientResponse.newBuilder()
                .setPatient(getGrpcPatientFromEntity(patient))
                .setExists(true)
                .build();
            responseObserver.onNext(patientResponse);
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllPatients(Empty request, StreamObserver<Users.PatientList> responseObserver) {
        this.logger.info("Called RPC Get All Patients");

        try {
            List<Patient> patients = patientRepository.findAll();
            List<Users.Patient> grpcPatientsList = patients.stream()
                .map(PatientService::getGrpcPatientFromEntity)
                .toList();
            Users.PatientList patientList = Users.PatientList.newBuilder()
                .addAllPatients(grpcPatientsList)
                .build();

            responseObserver.onNext(patientList);
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updatePatient(Users.Patient request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Update Patient");

        try {
            Patient patient = getEntityFromRequest(request);
            ResponseMessage message = patientRepository.update(patient);

            responseObserver.onNext(
                Users.isSuccessfulResponse.newBuilder()
                    .setIsSuccessful(true)
                    .setMessage(message.getMessage())
                    .build()
            );
        } catch (DoctorIsNotGpException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("doctor_not_gp").asRuntimeException());
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deletePatientById(Int32Value request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Delete Patient");

        int id = request.getValue();
        try {
            ResponseMessage message = patientRepository.delete(id);

            responseObserver.onNext(
                Users.isSuccessfulResponse.newBuilder()
                    .setIsSuccessful(true)
                    .setMessage(message.getMessage())
                    .build()
            );
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    private static Patient getEntityFromRequest(Users.Patient grpcPatient) throws DoctorIsNotGpException {
        Users.Doctor gp = grpcPatient.getGp();

        Doctor doctor = DoctorService.getEntityFromRequest(gp);

        return new Patient(
            grpcPatient.getFirstName(),
            grpcPatient.getLastName(),
            grpcPatient.getEmail(),
            grpcPatient.getPassword(),
            grpcPatient.getPin(),
            doctor,
            grpcPatient.getIsHealthInsured()
        );
    }

    private static Users.Patient getGrpcPatientFromEntity(Patient patient) {
        Users.Doctor gp = DoctorService.getGrpcDoctorFromEntity(patient.getDoctor());

        return Users.Patient.newBuilder()
            .setId(patient.getId())
            .setFirstName(patient.getFirstName())
            .setLastName(patient.getLastName())
            .setEmail(patient.getEmail())
            .setPassword(patient.getPassword())
            .setPin(patient.getPin())
            .setGp(gp)
            .setIsHealthInsured(patient.isHealthInsured())
            .build();
    }
}
