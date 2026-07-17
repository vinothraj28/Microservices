package com.microservices.profile.controllers.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.microservices.profile.dto.error.ErrorResponseDTO;
import com.microservices.profile.dto.error.ValidationErrorResponseDTO;
import com.microservices.profile.exceptions.AddressNotFoundException;
import com.microservices.profile.exceptions.DuplicateEmailException;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.exceptions.InvalidMfaChallengeException;
import com.microservices.profile.exceptions.MfaNotConfiguredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex,
                                         HttpServletRequest request){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(InvalidCredentialsException ex,
                                                                     HttpServletRequest request) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmail(DuplicateEmailException ex,
                                                                  HttpServletRequest request){
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handlesDataIntergity(DataIntegrityViolationException ex,
                                                                 HttpServletRequest req){
        String message = "Database constraint violation";

        Throwable rootCause = ex.getMostSpecificCause();
        String rootMessage = (rootCause != null ? rootCause.getMessage() : "");

        if (rootMessage.contains("uk_user_address_type")) {
            //log.error("Database failure while creating address", ex);
            message = "Address type already exists for this user";
        }
        if (rootMessage.contains("uk_user_email")) {
            //log.error("Database failure while creating user", ex);
            message = "Email already exists";
        }

        return buildErrorResponse(HttpStatus.CONFLICT, message, req.getRequestURI());
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAddressNotFound(AddressNotFoundException ex,
                                                                  HttpServletRequest request){
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handle invalid or expired MFA challenge token.
     * This occurs when a user tries to verify MFA with an invalid/expired challenge token.
     */
    @ExceptionHandler(InvalidMfaChallengeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidMfaChallenge(InvalidMfaChallengeException ex,
                                                                      HttpServletRequest request) {
        log.warn("MFA challenge verification failed: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handle MFA not configured for user.
     * This is a consistency error - the system issued an MFA challenge but user has no MFA credentials.
     */
    @ExceptionHandler(MfaNotConfiguredException.class)
    public ResponseEntity<ErrorResponseDTO> handleMfaNotConfigured(MfaNotConfiguredException ex,
                                                                   HttpServletRequest request) {
        log.error("MFA verification attempted but MFA not configured for user: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "MFA verification failed", request.getRequestURI());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
                                                                       HttpServletRequest req){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        return buildValidationErrorResponse(HttpStatus.BAD_REQUEST,errors, req.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidEnum(HttpMessageNotReadableException ex,
                                                              HttpServletRequest req){

        Throwable cause = ex.getCause();
        if(cause instanceof InvalidFormatException invalidFormatException){
            Class<?> targetType =
                    invalidFormatException.getTargetType();
            if(targetType.isEnum()){
                return buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        "Invalid enum value provided",
                        req.getRequestURI()
                );
            }
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", req.getRequestURI());
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
