package com.medrec.services;

import com.medrec.grpc.PatientServiceGrpc;
import com.medrec.grpc.SpecialtyServiceGrpc;
import com.medrec.persistence.specialty.SpecialtyRepository;

import java.util.logging.Logger;

public class SpecialtyServiceImpl extends SpecialtyServiceGrpc.SpecialtyServiceImplBase {
    private static SpecialtyServiceImpl instance;

    private final Logger logger = Logger.getLogger(SpecialtyServiceImpl.class.getName());

    private final SpecialtyRepository specialtyRepository = SpecialtyRepository.getInstance();

    private SpecialtyServiceImpl() {
    }

    public static SpecialtyServiceImpl getInstance() {
        if (instance == null) {
            instance = new SpecialtyServiceImpl();
        }
        return instance;
    }
}
