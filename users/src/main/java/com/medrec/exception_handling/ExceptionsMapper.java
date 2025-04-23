package com.medrec.exception_handling;

import com.medrec.exception_handling.exceptions.DatabaseConnectionException;
import com.medrec.exception_handling.exceptions.HibernateRuntimeException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class ExceptionsMapper {
    public static StatusRuntimeException toStatusRuntimeException(Throwable throwable) {
        if (throwable instanceof DatabaseConnectionException) {
            return Status.UNAVAILABLE.withDescription("Database Connection Error").asRuntimeException();
        } else if (throwable instanceof HibernateRuntimeException) {
            return Status.INTERNAL.withDescription("Database Error").asRuntimeException();
        }

        return Status.UNKNOWN.withDescription("Internal Server Error").asRuntimeException();
    }
}
