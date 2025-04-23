package com.medrec.exception_handling;

import com.medrec.exception_handling.exceptions.*;
import io.grpc.StatusRuntimeException;

public class ExceptionsMapper {
    public static RuntimeException translateStatusRuntimeException(StatusRuntimeException statusRuntimeException) {
        switch (statusRuntimeException.getStatus().getCode()) {
            case UNAVAILABLE:
                return new DatabaseConnectionException(statusRuntimeException.getStatus().getDescription());
            case INTERNAL:
                return new DatabaseException(statusRuntimeException.getStatus().getDescription());
            case ALREADY_EXISTS:
                return new AlreadyExistsException(statusRuntimeException.getStatus().getDescription());
            case UNKNOWN:
                return new ServiceException(statusRuntimeException.getStatus().getDescription());
            case ABORTED:
                return new UniqueConstrainException(statusRuntimeException.getStatus().getDescription());
            case NOT_FOUND:
                return new NotFoundException(statusRuntimeException.getStatus().getDescription());
            case INVALID_ARGUMENT:
                String message = statusRuntimeException.getStatus().getDescription();
                assert message != null;
                if (message.contains("doctor_not_gp")) {
                    return new DoctorNotGpException("Doctor Is Not Gp");
                } else {
                    return new InvalidPropertyException(message);
                }
            default:
                throw new ServiceException("Service Error");
        }
    }
}
