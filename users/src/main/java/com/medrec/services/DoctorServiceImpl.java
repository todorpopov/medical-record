package com.medrec.services;

import com.medrec.grpc.DoctorServiceGrpc;
import com.medrec.grpc.Users;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class DoctorServiceImpl extends DoctorServiceGrpc.DoctorServiceImplBase {
    private final Logger logger = Logger.getLogger(DoctorServiceImpl.class.getName());

    @Override
    public void createDoctor(Users.DoctorDetailsRequest request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Doctor");
        Users.isSuccessfulResponse resp = Users.isSuccessfulResponse
                .newBuilder()
                .setIsSuccessful(true)
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
