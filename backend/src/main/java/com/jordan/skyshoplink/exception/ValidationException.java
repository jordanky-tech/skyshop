package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {
    public ValidationException(String message, Object details) {
        super(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, details);
    }

    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }
}
