package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message);
    }

    public UnauthorizedException(String message, Object details) {
        super(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message, details);
    }
}
