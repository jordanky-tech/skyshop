package com.jordan.skyshoplink.exception;

import com.jordan.skyshoplink.common.response.ApiResponse;
import com.jordan.skyshoplink.common.response.ErrorDetail;
import com.jordan.skyshoplink.common.response.ResponseBuilder;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        ErrorDetail error = ErrorDetail.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();

        return ResponseBuilder.error(ex.getStatus(), ex.getMessage(), error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("field", err.getField());
                    m.put("message", err.getDefaultMessage());
                    return m;
                })
                .collect(Collectors.toList());

        ErrorDetail error = ErrorDetail.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .details(fieldErrors)
                .build();

        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Validation failed", error);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<Object>> handleConflict(ConflictException ex) {

        ErrorDetail error = ErrorDetail.builder()
                .code("CONFLICT_ERROR")
                .message("conflit en bd")
                .details(ex.getMessage())
                .build();

        return ResponseBuilder.error(HttpStatus.CONFLICT, "Validation failed", error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<Map<String, String>> violations = ex.getConstraintViolations().stream()
                .map(v -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("path", v.getPropertyPath().toString());
                    m.put("message", v.getMessage());
                    return m;
                })
                .collect(Collectors.toList());

        ErrorDetail error = ErrorDetail.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .details(violations)
                .build();

        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Validation failed", error);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        ErrorDetail error = ErrorDetail.builder()
                .code("FORBIDDEN")
                .message(ex.getMessage())
                .build();
        return ResponseBuilder.error(HttpStatus.FORBIDDEN, ex.getMessage(), error);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<Object>> handleAuthentication(AuthenticationException ex) {
        ErrorDetail error = ErrorDetail.builder()
                .code("UNAUTHORIZED")
                .message(ex.getMessage())
                .build();
        return ResponseBuilder.error(HttpStatus.UNAUTHORIZED, ex.getMessage(), error);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ErrorDetail error = ErrorDetail.builder()
                .code("METHOD_NOT_ALLOWED")
                .message(ex.getMessage())
                .build();
        return ResponseBuilder.error(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), error);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {
        ErrorDetail error = ErrorDetail.builder()
                .code("NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, ex.getMessage(), error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception exception){
        ErrorDetail error = ErrorDetail.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("Internal server error")
                .details(exception.getMessage())
                .build();

        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorDetail error = ErrorDetail.builder()
                .code("UNAUTHORIZED")
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();

        return ResponseBuilder.error(HttpStatus.UNAUTHORIZED, "non authentifier", error);

    }
}
