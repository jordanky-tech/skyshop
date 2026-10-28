package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
    }

    public ForbiddenException(String message, Object details) {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN", message, details);
    }
}
