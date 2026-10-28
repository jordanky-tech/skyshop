package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", message);
    }

    public UserAlreadyExistsException(String message, Object details) {
        super(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", message, details);
    }
}
