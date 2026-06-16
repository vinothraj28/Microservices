package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.errors.ErrorResponseDTO;
import com.microservices.gateway.DTOS.errors.ValidationErrorResponseDTO;
import com.microservices.gateway.excpetions.user.DuplicateEmailException;
import com.microservices.gateway.excpetions.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class UserExceptionController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex,
                                                               ServerWebExchange exchange){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange.getRequest().getPath().value());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmail(DuplicateEmailException ex,
                                                                 ServerWebExchange exchange){
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), exchange.getRequest().getPath().value());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidation(WebExchangeBindException ex,
                                                                       ServerWebExchange exchange){
        Map<String, String> errors = new HashMap<>();
        log.info("Invalid Argument Error Occurred {}",errors);
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        log.info("Invalid Argument Error Occurred {}",errors);
        return buildValidationErrorResponse(HttpStatus.BAD_REQUEST,errors, exchange.getRequest().getPath().value());
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

    public ResponseEntity<ValidationErrorResponseDTO>
    buildValidationErrorResponse(HttpStatus status, Map<String, String> errors, String path){

        ValidationErrorResponseDTO validationErrorResponseDTO = new ValidationErrorResponseDTO(
                status.value(),
                "Validation error",
                path,
                errors,
                Instant.now(),
                UUID.randomUUID().toString()
        );

        return ResponseEntity.status(status).body(validationErrorResponseDTO);
    }

}
