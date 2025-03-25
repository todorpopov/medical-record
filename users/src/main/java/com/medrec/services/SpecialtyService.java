package com.medrec.services;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.medrec.grpc.users.SpecialtyServiceGrpc;
import com.medrec.grpc.users.Users;
import com.medrec.persistence.ResponseMessage;
import com.medrec.persistence.specialty.Specialty;
import com.medrec.persistence.specialty.SpecialtyRepository;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;

public class SpecialtyService extends SpecialtyServiceGrpc.SpecialtyServiceImplBase {
    private static SpecialtyService instance;

    private final Logger logger = Logger.getLogger(SpecialtyService.class.getName());

    private final SpecialtyRepository specialtyRepository = SpecialtyRepository.getInstance();

    private SpecialtyService() {
    }

    public static SpecialtyService getInstance() {
        if (instance == null) {
            instance = new SpecialtyService();
        }
        return instance;
    }

    @Override
    public void createSpecialty(Users.Specialty request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Specialty");

        Specialty specialty = new Specialty(
            request.getSpecialtyName(),
            request.getSpecialtyDescription()
        );

        ResponseMessage message = specialtyRepository.save(specialty);
        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(message.isSuccessful())
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getSpecialtyById(Int32Value request, StreamObserver<Users.SpecialtyResponse> responseObserver) {
        this.logger.info("Called RPC Get Specialty By Id");

        int id = request.getValue();
        Specialty specialty = specialtyRepository.findById(id);

        if (specialty == null) {
            responseObserver.onNext(Users.SpecialtyResponse.newBuilder().setExists(false).build());
        } else {
            Users.SpecialtyResponse specialtyResponse = Users.SpecialtyResponse.newBuilder()
                .setSpecialty(getGrpcSpecialtyFromEntity(specialty))
                .setExists(true)
                .build();
            responseObserver.onNext(specialtyResponse);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getAllSpecialties(Empty request, StreamObserver<Users.SpecialtiesList> responseObserver) {
        this.logger.info("Called RPC Get All Specialties");

        List<Specialty> specialties = specialtyRepository.findAll();
        List<Users.Specialty> grpcSpecialtiesList = specialties.stream()
            .map(SpecialtyService::getGrpcSpecialtyFromEntity)
            .toList();
        Users.SpecialtiesList specialtiesList = Users.SpecialtiesList.newBuilder()
            .addAllSpecialties(grpcSpecialtiesList)
            .build();

        responseObserver.onNext(specialtiesList);
        responseObserver.onCompleted();
    }

    @Override
    public void updateSpecialty(Users.Specialty request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Update Specialty");

        Specialty specialty = getEntityFromRequest(request);
        ResponseMessage message = specialtyRepository.update(specialty);

        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(message.isSuccessful())
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void deleteSpecialtyById(Int32Value request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Delete Specialty");

        int id = request.getValue();
        ResponseMessage message = specialtyRepository.delete(id);

        responseObserver.onNext(
            Users.isSuccessfulResponse.newBuilder()
                .setIsSuccessful(true)
                .setMessage(message.getMessage())
                .build()
        );
        responseObserver.onCompleted();
    }

    private static Specialty getEntityFromRequest(Users.Specialty grpcSpecialty) {
        return new Specialty(
            grpcSpecialty.getSpecialtyName(),
            grpcSpecialty.getSpecialtyDescription()
        );
    }

    private static Users.Specialty getGrpcSpecialtyFromEntity(Specialty specialty) {
        return Users.Specialty.newBuilder()
            .setId(specialty.getId())
            .setSpecialtyName(specialty.getSpecialtyName())
            .setSpecialtyDescription(specialty.getSpecialtyDescription())
            .build();
    }
}
