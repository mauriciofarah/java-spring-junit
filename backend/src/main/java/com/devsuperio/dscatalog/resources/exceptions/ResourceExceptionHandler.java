package com.devsuperio.dscatalog.resources.exceptions;

import com.devsuperio.dscatalog.services.exceptions.DataBaseIntegrityException;
import com.devsuperio.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(
                Instant.now(),
                httpStatus.value(),
                "Resource not found",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(err);
    }

    @ExceptionHandler(DataBaseIntegrityException.class)
    public ResponseEntity<StandardError> DataBaseIntegrity(DataBaseIntegrityException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                httpStatus.value(),
                "Database exception",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(err);
    }
}
