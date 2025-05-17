package com.medrec.exception_handling;

import com.medrec.exception_handling.exceptions.BadRequestException;
import com.medrec.exception_handling.exceptions.DatabaseConnectionException;
import com.medrec.exception_handling.exceptions.DatabaseException;
import com.medrec.exception_handling.exceptions.NotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExceptionsMapper {
    public static StatusRuntimeException toStatusRuntimeException(Throwable throwable) {
        Logger logger = Logger.getLogger(ExceptionsMapper.class.getName());
        String message = throwable.getMessage();

        if (throwable instanceof DatabaseConnectionException) {
            logger.info("Database connection exception");
            return Status.UNAVAILABLE.withDescription("Database Connection Error").asRuntimeException();
        } else if (throwable instanceof DatabaseException) {
            logger.info("Database exception");
            return Status.INTERNAL.withDescription("Database Error").asRuntimeException();
        } else if (throwable instanceof BadRequestException) {
            logger.info("Bad request exception");
            if(message.contains("date_time_not_parsed")) {
                return Status.ABORTED.withDescription("Datetime not parsed correctly").asRuntimeException();
            } else if (message.contains("doctor_not_free")) {
                return Status.ABORTED.withDescription("Doctor is not free at the specified time").asRuntimeException();
            } else if(message.contains("invalid_id")) {
                return Status.ABORTED.withDescription("Invalid id").asRuntimeException();
            } else if (message.contains("invalid_status")) {
                return Status.ABORTED.withDescription("Invalid status").asRuntimeException();
            } else {
                return Status.ABORTED.withDescription("Bad request").asRuntimeException();
            }
        } else if (throwable instanceof NotFoundException) {
            logger.info("Not found exception");
            if (message.contains("appointment_not_found")) {
                return Status.NOT_FOUND.withDescription("Appointment Not Found").asRuntimeException();
            } else {
                return Status.NOT_FOUND.withDescription("Entity Not Found").asRuntimeException();
            }
        }

        logger.severe("Unknown exception found: " + message);
        return Status.UNKNOWN.withDescription("Internal Server Error").asRuntimeException();
    }
}
