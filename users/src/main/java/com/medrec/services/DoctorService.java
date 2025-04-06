package com.medrec.services;

import com.medrec.dtos.CreateDoctorSpecialtyIdDTO;
import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.Users;
import com.google.protobuf.Empty;
import com.medrec.persistence.ResponseMessage;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.doctor.DoctorRepository;
import com.medrec.persistence.specialty.Specialty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DoctorService extends DoctorServiceGrpc.DoctorServiceImplBase {
    private static DoctorService instance;

    private final Logger logger = Logger.getLogger(DoctorService.class.getName());

    private final DoctorRepository doctorRepository = DoctorRepository.getInstance();

    private DoctorService() {
    }

    public static DoctorService getInstance() {
        if (instance == null) {
            instance = new DoctorService();
        }
        return instance;
    }

    @Override
    public void createDoctor(Users.Doctor request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Doctor");

        Doctor doctor = getEntityFromRequest(request);

        ResponseMessage message = doctorRepository.save(doctor);

        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(message.isSuccessful())
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void createDoctorSpecialtyId(Users.DoctorSpecialtyId request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Doctor- Specialty Id");
        CreateDoctorSpecialtyIdDTO dto = new CreateDoctorSpecialtyIdDTO(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPassword(),
            request.getSpecialtyId(),
            request.getIsGp()
        );

        ResponseMessage message = doctorRepository.saveWithSpecialtyId(dto);
        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(message.isSuccessful())
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getDoctorById(Int32Value request, StreamObserver<Users.DoctorResponse> responseObserver) {
        this.logger.info("Called RPC Get Doctor By Id");

        int id = request.getValue();
        Doctor doctor = doctorRepository.findById(id);

        if (doctor == null) {
            responseObserver.onNext(Users.DoctorResponse.newBuilder().setExists(false).build());
        } else {
            Users.DoctorResponse doctorResponse = Users.DoctorResponse.newBuilder()
                .setDoctor(getGrpcDoctorFromEntity(doctor))
                .setExists(true)
                .build();
            responseObserver.onNext(doctorResponse);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getDoctorByEmail(StringValue request, StreamObserver<Users.DoctorResponse> responseObserver) {
        this.logger.info("Called RPC Get Doctor By Email");

        String email = request.getValue();
        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) {
            responseObserver.onNext(Users.DoctorResponse.newBuilder().setExists(false).build());
        } else {
            Users.DoctorResponse doctorResponse = Users.DoctorResponse.newBuilder()
                .setDoctor(getGrpcDoctorFromEntity(doctor))
                .setExists(true)
                .build();
            responseObserver.onNext(doctorResponse);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getAllGPDoctors(Empty request, StreamObserver<com.medrec.grpc.users.Users.DoctorList> responseObserver) {
        this.logger.info("Called RPC Get All GP Doctors");

        List<Doctor> doctors = doctorRepository.findAllGpDoctors();
        List<Users.Doctor> grpcDocctors = doctors.stream()
            .map(DoctorService::getGrpcDoctorFromEntity)
            .toList();
        Users.DoctorList list = Users.DoctorList.newBuilder()
            .addAllDoctors(grpcDocctors)
            .build();

        responseObserver.onNext(list);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDoctors(Empty request, StreamObserver<com.medrec.grpc.users.Users.DoctorList> responseObserver) {
        this.logger.info("Called RPC Get All Doctors");

        List<Doctor> doctors = doctorRepository.findAll();
        List<Users.Doctor> grpcDoctorsList = doctors.stream()
            .map(DoctorService::getGrpcDoctorFromEntity)
            .toList();
        Users.DoctorList doctorList = Users.DoctorList.newBuilder()
            .addAllDoctors(grpcDoctorsList)
            .build();

        responseObserver.onNext(doctorList);
        responseObserver.onCompleted();
    }

    @Override
    public void updateDoctor(Users.Doctor request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Update Doctor");

        Doctor doctor = getEntityFromRequest(request);
        ResponseMessage message = doctorRepository.update(doctor);

        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(true)
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void deleteDoctorById(Int32Value request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Delete Doctor");

        int id = request.getValue();
        ResponseMessage message = doctorRepository.delete(id);

        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(true)
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    static Doctor getEntityFromRequest(Users.Doctor grpcDoctor) {
        Users.Specialty requestSpecialty = grpcDoctor.getSpecialty();

        Specialty specialty = new Specialty(
            requestSpecialty.getId(),
            requestSpecialty.getSpecialtyName(),
            requestSpecialty.getSpecialtyDescription()
        );

        return new Doctor(
            grpcDoctor.getFirstName(),
            grpcDoctor.getLastName(),
            grpcDoctor.getEmail(),
            grpcDoctor.getPassword(),
            specialty,
            grpcDoctor.getIsGp()
        );
    }

    static Users.Doctor getGrpcDoctorFromEntity(Doctor doctor) {
        Users.Specialty grpcSpecialty = Users.Specialty.newBuilder()
            .setId(doctor.getSpecialty().getId())
            .setSpecialtyName(doctor.getSpecialty().getSpecialtyName())
            .setSpecialtyDescription(doctor.getSpecialty().getSpecialtyDescription())
            .build();

        return Users.Doctor.newBuilder()
            .setId(doctor.getId())
            .setFirstName(doctor.getFirstName())
            .setLastName(doctor.getLastName())
            .setEmail(doctor.getEmail())
            .setPassword(doctor.getPassword())
            .setSpecialty(grpcSpecialty)
            .setIsGp(doctor.isGp())
            .build();
    }
}
