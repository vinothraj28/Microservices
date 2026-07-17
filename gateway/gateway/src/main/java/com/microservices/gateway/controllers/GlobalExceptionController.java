package com.microservices.gateway.controllers;


import com.microservices.gateway.DTOS.errors.ErrorResponseDTO;
import com.microservices.gateway.excpetions.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    /**
     * Handle authentication failures (invalid credentials, MFA code, etc.)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(
            AuthenticationException ex,
            ServerHttpRequest request) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getURI().getPath()
        );
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleGrpcException(
            StatusRuntimeException ex,
            ServerHttpRequest request) {

        Status.Code code = ex.getStatus().getCode();

        if (code == Status.Code.ALREADY_EXISTS) {
            return buildErrorResponse(
                    HttpStatus.CONFLICT,
                    ex.getStatus().getDescription(),
                    request.getURI().getPath()
            );
        }

        if (code == Status.Code.NOT_FOUND) {
            return buildErrorResponse(
                    HttpStatus.NOT_FOUND,
                    ex.getStatus().getDescription(),
                    request.getURI().getPath()
            );
        }

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getStatus().getDescription(),
                request.getURI().getPath()
        );
    }

    public ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String message, String path){
        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                message,
                path,
                Instant.now(),
                UUID.randomUUID().toString()
        );
        return ResponseEntity.status(status).body(error);

    }
}
