package com.harts.bank.api.exception.handler;

import com.harts.bank.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomerControllerAdvice {

    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String EXCEPTION = "exception";

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        response.put(ERROR, "Customer Not Found");
        response.put(MESSAGE, ex.getMessage());
        response.put(TIMESTAMP, java.time.Instant.now());
        response.put(EXCEPTION, ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueConstraintException.class)
    public ResponseEntity<Map<String, Object>> handleUniqueConstraintException(UniqueConstraintException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Unique Constraint Violation");
        response.put(MESSAGE, ex.getMessage());
        response.put(TIMESTAMP, java.time.Instant.now());
        response.put(EXCEPTION, ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Validation Failed");
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        response.put("validationErrors", errors);
        response.put(TIMESTAMP, java.time.Instant.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequestException(InvalidRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Invalid Request");
        response.put(MESSAGE, ex.getMessage());
        response.put(TIMESTAMP, java.time.Instant.now());
        response.put(EXCEPTION, ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFoundException(AccountNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        response.put(ERROR, "Account Not Found");
        response.put(MESSAGE, ex.getMessage());
        response.put(TIMESTAMP, java.time.Instant.now());
        response.put(EXCEPTION, ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateAccountException(DuplicateAccountException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Duplicate Account");
        response.put(MESSAGE, ex.getMessage());
        response.put(TIMESTAMP, java.time.Instant.now());
        response.put(EXCEPTION, ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InActiveAccountException.class)
    public ResponseEntity<Map<String, Object>> handleInActiveAccountException(InActiveAccountException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Inactive Account");
        response.put(MESSAGE, ex.getMessage());
        response.put(TIMESTAMP, java.time.Instant.now());
        response.put(EXCEPTION, ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
