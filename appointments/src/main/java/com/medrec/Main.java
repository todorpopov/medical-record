package com.medrec;

import com.medrec.persistence.appointment.Appointment;
import com.medrec.persistence.appointment.AppointmentsRepository;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        GrpcServer grpcServer = new GrpcServer();

        try {
            grpcServer.start();
        } catch (IOException e) {
            logger.severe(String.format("Error occurred while starting the gRPC server: %s", e.getMessage()));
        } catch (InterruptedException e) {
            logger.severe(String.format("Error occurred while terminating the gRPC server: %s", e.getMessage()));
        }
    }
}
