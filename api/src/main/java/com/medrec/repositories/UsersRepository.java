package com.medrec.repositories;

import com.medrec.grpc.DoctorServiceGrpc;
import com.medrec.grpc.Users;
import com.medrec.utils.ResponseMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class UsersRepository {
    private final ManagedChannel channel;

    public UsersRepository() {
        int port = Integer.parseInt(System.getenv("USERS_PORT"));
        String host = System.getenv("USERS_HOST");
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    public ResponseMessage createDoctor(Users.Doctor doctor) {
        DoctorServiceGrpc.DoctorServiceBlockingStub stub = DoctorServiceGrpc.newBlockingStub(channel);
        Users.isSuccessfulResponse response = stub.createDoctor(doctor);
        return new ResponseMessage(response.getIsSuccessful(), response.getMessage());
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Shutting down gRPC client channel...");
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            channel.shutdownNow();
        }
    }
}

