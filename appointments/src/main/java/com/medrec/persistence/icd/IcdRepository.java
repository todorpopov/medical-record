package com.medrec.persistence.icd;

import java.util.logging.Logger;

public class IcdRepository {
    public static IcdRepository instance;

    private final Logger logger = Logger.getLogger(IcdRepository.class.getName());

    private IcdRepository() {}

    public static IcdRepository getInstance() {
        if (instance == null) {
            instance = new IcdRepository();
        }
        return instance;
    }
}
