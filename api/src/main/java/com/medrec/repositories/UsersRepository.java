package com.medrec.repositories;

import com.medrec.grpc.DoctorServiceGrpc;
import com.medrec.grpc.Users;
import com.medrec.utils.ResponseMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepository {
    public ResponseMessage createDoctor(Users.Doctor doctor) {
        int port = Integer.parseInt(System.getenv("USERS_PORT"));
        String host = System.getenv("USERS_HOST");

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        DoctorServiceGrpc.DoctorServiceBlockingStub stub = DoctorServiceGrpc.newBlockingStub(channel);

        Users.isSuccessfulResponse response = stub.createDoctor(doctor);

        return new ResponseMessage(response.getIsSuccessful(), response.getMessage());
    }
}

