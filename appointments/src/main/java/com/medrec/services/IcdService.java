package com.medrec.services;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.medrec.grpc.users.Appointments;
import com.medrec.grpc.users.IcdServiceGrpc;
import com.medrec.persistence.icd.Icd;
import com.medrec.persistence.icd.IcdRepository;
import com.medrec.utils.Utils;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class IcdService extends IcdServiceGrpc.IcdServiceImplBase {
    public static IcdService instance;

    private final Logger logger = Logger.getLogger(IcdService.class.getName());

    private final IcdRepository icdRepository = IcdRepository.getInstance();

    private IcdService() {}

    public static IcdService getInstance() {
        if (instance == null) {
            instance = new IcdService();
        }
        return instance;
    }

    @Override
    public void createIcd(Appointments.CreateIcdRequest request, StreamObserver<Appointments.Icd> responseObserver) {
        this.logger.info("Called RPC Create ICD");

        try {
            Icd icd = this.icdRepository.save(request.getCode(), request.getDescription());

            responseObserver.onNext(Utils.getIcdFromDomainModel(icd));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getIcdById(Int32Value request, StreamObserver<Appointments.Icd> responseObserver) {
        int id = request.getValue();
        this.logger.info("Called RPC Get ICD By Id: " + id);

        try {
            Icd icd = this.icdRepository.getById(id);
            responseObserver.onNext(Utils.getIcdFromDomainModel(icd));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllIcdEntities(Empty request, StreamObserver<Appointments.IcdEntitiesList> responseObserver) {
        this.logger.info("Called RPC Get All ICD Entities");

        try {
            List<Icd> icds = this.icdRepository.findAll();
            List<Appointments.Icd> grpcIcds = icds.stream()
                .map(Utils::getIcdFromDomainModel)
                .toList();

            Appointments.IcdEntitiesList list = Appointments.IcdEntitiesList.newBuilder()
                .addAllIcdEntities(grpcIcds)
                .build();

            responseObserver.onNext(list);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateIcd(Appointments.UpdateIcdRequest request, StreamObserver<Appointments.Icd> responseObserver) {
        int id = request.getId();
        this.logger.info("Called RPC update ICD for Id: " + id);

        try {
            Icd icd = this.icdRepository.update(
                request.getId(),
                Optional.of(request.getCode()),
                Optional.of(request.getDescription())
            );

            responseObserver.onNext(Utils.getIcdFromDomainModel(icd));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteIcdById(Int32Value request, StreamObserver<Empty> responseObserver) {
        int id = request.getValue();
        this.logger.info("Called RPC Delete ICD By Id: " + id);

        try {
            this.icdRepository.delete(id);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(e);
        }
    }
}
