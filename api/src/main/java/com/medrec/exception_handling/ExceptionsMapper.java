package com.medrec.exception_handling;

import com.medrec.exception_handling.exceptions.DatabaseConnectionException;
import com.medrec.exception_handling.exceptions.HibernateException;
import com.medrec.exception_handling.exceptions.ServiceException;
import io.grpc.StatusRuntimeException;

public class ExceptionsMapper {
    public static RuntimeException translateStatusRuntimeException(StatusRuntimeException statusRuntimeException) {
        switch (statusRuntimeException.getStatus().getCode()) {
            case UNAVAILABLE:
                return new DatabaseConnectionException(statusRuntimeException.getStatus().getDescription());
            case INTERNAL:
                return new HibernateException(statusRuntimeException.getStatus().getDescription());
            case UNKNOWN:
                return new ServiceException(statusRuntimeException.getStatus().getDescription());
            default:
                throw new ServiceException("Service Error");
        }
    }
}
