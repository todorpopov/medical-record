package com.medrec.services;

import com.medrec.grpc.DoctorServiceGrpc;
import com.medrec.grpc.Users;
import com.google.protobuf.Empty;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.doctor.DoctorRepository;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;

public class DoctorServiceImpl extends DoctorServiceGrpc.DoctorServiceImplBase {
    private static DoctorServiceImpl instance;

    private final Logger logger = Logger.getLogger(DoctorServiceImpl.class.getName());

    private final DoctorRepository doctorRepository = DoctorRepository.getInstance();

    private DoctorServiceImpl() {
    }

    public static DoctorServiceImpl getInstance() {
        if (instance == null) {
            instance = new DoctorServiceImpl();
        }
        return instance;
    }

    @Override
    public void createDoctor(Users.Doctor request, StreamObserver<Users.isSuccessfulResponse> responseObserver) {
        this.logger.info("Called RPC Create Doctor");

        Users.isSuccessfulResponse resp = Users.isSuccessfulResponse
                .newBuilder()
                .setIsSuccessful(true)
                .setMessage("Success")
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDoctors(Empty request, StreamObserver<com.medrec.grpc.Users.DoctorList> responseObserver) {
        this.logger.info("Called RPC Get All Doctors");

        List<Doctor> doctors = doctorRepository.findAll();

        List<Users.Doctor> doctorList = doctors.stream()
                .map(doctor -> Users.Doctor.newBuilder()
                        .setId(doctor.getId())
                        .setFirstName(doctor.getFirstName())
                        .setLastName(doctor.getLastName())
                        .setEmail(doctor.getEmail())
                        .setPassword(doctor.getPassword())
                        .setIsGp(doctor.isGp())
                        .build())
                .toList();

        Users.DoctorList doctorsList = Users.DoctorList.newBuilder().addAllDoctors(doctorList).build();

        responseObserver.onNext(doctorsList);
        responseObserver.onCompleted();
    }
}
