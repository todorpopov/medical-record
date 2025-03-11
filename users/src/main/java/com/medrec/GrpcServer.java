package com.medrec;

import com.medrec.services.DoctorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class GrpcServer {
    private final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    public void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new DoctorServiceImpl())
                .build();

        server.start();
        logger.info("gRPC server started on port 9090");

        server.awaitTermination();
        logger.info("gRPC server terminated");
    }
}
