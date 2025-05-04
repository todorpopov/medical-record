package com.medrec.services;

import com.medrec.dtos.CreateDoctorDTO;
import com.medrec.exception_handling.ExceptionsMapper;
import com.medrec.grpc.users.DoctorServiceGrpc;
import com.medrec.grpc.users.Users;
import com.google.protobuf.Empty;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.doctor.DoctorRepository;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;

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
    public void createDoctor(Users.CreateDoctorRequest request, StreamObserver<Users.Doctor> responseObserver) {
        this.logger.info("Called RPC Create Doctor- Specialty Id");
        CreateDoctorDTO dto = new CreateDoctorDTO(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPassword(),
            request.getSpecialtyId(),
            request.getIsGp()
        );

        try {
            Doctor savedDoctor = doctorRepository.save(dto);
            responseObserver.onNext(grpcFromDomainModel(savedDoctor)
            );
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getDoctorById(Int32Value request, StreamObserver<Users.Doctor> responseObserver) {
        this.logger.info("Called RPC Get Doctor By Id");
        int id = request.getValue();

        try {
            Doctor doctor = doctorRepository.findById(id);
            responseObserver.onNext(grpcFromDomainModel(doctor));
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getDoctorByEmail(StringValue request, StreamObserver<Users.Doctor> responseObserver) {
        this.logger.info("Called RPC Get Doctor By Email");
        String email = request.getValue();

        try {
            Doctor doctor = doctorRepository.findByEmail(email);
            responseObserver.onNext(grpcFromDomainModel(doctor));
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllGPDoctors(Empty request, StreamObserver<com.medrec.grpc.users.Users.DoctorList> responseObserver) {
        this.logger.info("Called RPC Get All GP Doctors");

        try {
            List<Doctor> doctors = doctorRepository.findAllGpDoctors();
            List<Users.Doctor> grpcDoctors = doctors.stream()
                .map(DoctorService::grpcFromDomainModel)
                .toList();
            Users.DoctorList list = Users.DoctorList.newBuilder()
                .addAllDoctors(grpcDoctors)
                .build();

            responseObserver.onNext(list);
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllDoctors(Empty request, StreamObserver<com.medrec.grpc.users.Users.DoctorList> responseObserver) {
        this.logger.info("Called RPC Get All Doctors");

        try {
            List<Doctor> doctors = doctorRepository.findAll();
            List<Users.Doctor> grpcDoctorsList = doctors.stream()
                .map(DoctorService::grpcFromDomainModel)
                .toList();
            Users.DoctorList doctorList = Users.DoctorList.newBuilder()
                .addAllDoctors(grpcDoctorsList)
                .build();

            responseObserver.onNext(doctorList);
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateDoctor(Users.UpdateDoctorRequest request, StreamObserver<Users.Doctor> responseObserver) {
        this.logger.info("Called RPC Update Doctor");

        try {
            Doctor updatedDoctor = doctorRepository.update(request);
            responseObserver.onNext(grpcFromDomainModel(updatedDoctor));
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteDoctorById(Int32Value request, StreamObserver<Empty> responseObserver) {
        this.logger.info("Called RPC Delete Doctor");
        int id = request.getValue();

        try {
            doctorRepository.delete(id);
            responseObserver.onNext(Empty.getDefaultInstance());
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        } finally {
            responseObserver.onCompleted();
        }
    }

    static Users.Doctor grpcFromDomainModel(Doctor doctor) {
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
