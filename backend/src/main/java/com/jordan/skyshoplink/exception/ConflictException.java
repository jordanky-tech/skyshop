package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, "CONFLICT", message);
    }

    public ConflictException(String message, Object details) {
        super(HttpStatus.CONFLICT, "CONFLICT", message, details);
    }
}
