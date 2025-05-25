package com.medrec.persistence.leave;

import java.util.logging.Logger;

public class SickLeaveRepository {
    public static SickLeaveRepository instance;

    private final Logger logger = Logger.getLogger(SickLeaveRepository.class.getName());

    private SickLeaveRepository() {}

    public static SickLeaveRepository getInstance() {
        if (instance == null) {
            instance = new SickLeaveRepository();
        }
        return instance;
    }
}
