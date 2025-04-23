package com.medrec.exception_handling;

import com.medrec.exception_handling.exceptions.DatabaseConnectionException;
import com.medrec.exception_handling.exceptions.DatabaseException;
import com.medrec.exception_handling.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHTTPExceptionHandler {
    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<ErrorHTTPResponse> handleDatabaseConnectionException(DatabaseConnectionException e) {
        ErrorHTTPResponse error = new ErrorHTTPResponse("DATABASE_CONNECTION_ERROR", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHTTPResponse> handleHibernateException(DatabaseException e) {
        ErrorHTTPResponse error = new ErrorHTTPResponse("HIBERNATE_EXCEPTION", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ErrorHTTPResponse> handleServiceException(ServiceException e) {
        ErrorHTTPResponse error = new ErrorHTTPResponse("SERVICE_EXCEPTION", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
