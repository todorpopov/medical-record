package com.medrec.services;

import com.medrec.grpc.PatientServiceGrpc;
import com.medrec.persistence.patient.PatientRepository;

import java.util.logging.Logger;

public class PatientServiceImpl extends PatientServiceGrpc.PatientServiceImplBase {
    private static PatientServiceImpl instance;

    private final Logger logger = Logger.getLogger(PatientServiceImpl.class.getName());

    private final PatientRepository patientRepository = PatientRepository.getInstance();

    private PatientServiceImpl() {
    }

    public static PatientServiceImpl getInstance() {
        if (instance == null) {
            instance = new PatientServiceImpl();
        }
        return instance;
    }
}
