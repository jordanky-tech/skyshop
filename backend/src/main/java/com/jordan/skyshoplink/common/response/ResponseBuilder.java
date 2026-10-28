package com.jordan.skyshoplink.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {

        Meta meta = Meta.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .timestamp(Instant.now())
                .build();

        ApiResponse<T> response = ApiResponse.<T>builder()
                .meta(meta)
                .data(data)
                .error(null)
                .build();

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error( HttpStatus status,String message, Object error ) {

        Meta meta = Meta.builder()
                .status(status.value())
                .message(message)
                .timestamp(Instant.now())
                .build();

        ApiResponse<T> response = ApiResponse.<T>builder()
                .meta(meta)
                .data(null)
                .error(error)
                .build();

        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {

        Meta meta = Meta.builder()
                .status(HttpStatus.CREATED.value())
                .message(message)
                .timestamp(Instant.now())
                .build();

        ApiResponse<T> response = ApiResponse.<T>builder()
                .meta(meta)
                .data(data)
                .error(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}