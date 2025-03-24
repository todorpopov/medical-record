package com.medrec.gateways;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Repository
public class AuthGateway {
    private final Logger logger = Logger.getLogger(AuthGateway.class.getName());
    private final ManagedChannel channel;

    public AuthGateway() {
        int port = Integer.parseInt(System.getenv("AUTH_PORT"));
        String host = System.getenv("AUTH_HOST");
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }


    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down channel");
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            channel.shutdownNow();
        }
    }
}
