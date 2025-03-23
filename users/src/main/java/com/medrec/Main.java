package com.medrec;

import com.medrec.exceptions.DoctorIsNotGpException;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.doctor.DoctorRepository;
import com.medrec.persistence.patient.Patient;
import com.medrec.persistence.patient.PatientRepository;
import com.medrec.persistence.specialty.Specialty;
import com.medrec.persistence.specialty.SpecialtyRepository;

import java.io.IOException;
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
