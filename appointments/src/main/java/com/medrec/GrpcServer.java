package com.medrec;

import com.medrec.services.AppointmentsService;
import com.medrec.services.DiagnosesService;
import com.medrec.services.IcdService;
import com.medrec.services.SickLeaveService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.HealthStatusManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GrpcServer {
    private final Logger logger = Logger.getLogger(GrpcServer.class.getName());
    private final HealthStatusManager healthStatusManager = new HealthStatusManager();

    public void start() throws IOException, InterruptedException {
        int port = Integer.parseInt(System.getenv("APPOINTMENTS_PORT"));
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Server server = ServerBuilder.forPort(port)
            .executor(executor)
            .addService(IcdService.getInstance())
            .addService(SickLeaveService.getInstance())
            .addService(DiagnosesService.getInstance())
            .addService(AppointmentsService.getInstance())
            .addService(healthStatusManager.getHealthService())
            .build();

        healthStatusManager.setStatus("appointments-service", HealthCheckResponse.ServingStatus.SERVING);

        server.start();
        logger.info(String.format("gRPC server started on port %s", port));

        server.awaitTermination();
        logger.info("gRPC server terminated");
    }
}
