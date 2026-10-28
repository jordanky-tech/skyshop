package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", message);
    }

    public InvalidCredentialsException(String message, Object details) {
        super(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", message, details);
    }
}
