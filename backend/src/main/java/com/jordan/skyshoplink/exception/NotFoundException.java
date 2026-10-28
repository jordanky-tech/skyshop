package com.jordan.skyshoplink.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "NOT_FOUND", "Ressource introuvable");
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }
}
