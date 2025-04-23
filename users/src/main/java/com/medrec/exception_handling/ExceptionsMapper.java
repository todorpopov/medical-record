package com.medrec.exception_handling;

import com.medrec.exception_handling.exceptions.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExceptionsMapper {
    public static StatusRuntimeException toStatusRuntimeException(Throwable throwable) {
        Logger logger = Logger.getLogger(ExceptionsMapper.class.getName());

        if (throwable instanceof DatabaseConnectionException) {
            logger.info("Database connection exception");
            return Status.UNAVAILABLE.withDescription("Database Connection Error").asRuntimeException();
        } else if (throwable instanceof DatabaseException) {
            logger.info("Database exception");
            return Status.INTERNAL.withDescription("Database Error").asRuntimeException();
        } else if (throwable instanceof AlreadyExistsException) {
            logger.info("Already exists exception");
            return Status.ALREADY_EXISTS.withDescription("Entity Already Exists").asRuntimeException();
        } else if (throwable instanceof ConstrainException) {
            logger.info("Unique constraint exception");
            return Status.ABORTED.withDescription("Constraint Violation").asRuntimeException();
        } else if (throwable instanceof NotFoundException) {
            logger.info("Not found exception");
            return Status.NOT_FOUND.withDescription("Entity Not Found").asRuntimeException();
        } else if (throwable instanceof InvalidPropertyException) {
            logger.info("Invalid property exception");
            return Status.INVALID_ARGUMENT.withDescription("Invalid Property").asRuntimeException();
        }

        logger.severe("Unknown exception found: " + throwable.getMessage());
        return Status.UNKNOWN.withDescription("Internal Server Error").asRuntimeException();
    }
}
