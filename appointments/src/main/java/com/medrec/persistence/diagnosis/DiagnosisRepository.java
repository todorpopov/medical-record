package com.medrec.persistence.diagnosis;
import java.util.logging.Logger;

public class DiagnosisRepository {
    private static DiagnosisRepository instance;

    private final Logger logger = Logger.getLogger(DiagnosisRepository.class.getName());

    private DiagnosisRepository() {
    }

    public static DiagnosisRepository getInstance() {
        if (instance == null) {
            instance = new DiagnosisRepository();
        }
        return instance;
    }
}
