package com.microservices.profile.controllers.gRPcExceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.microservices.profile.dto.error.ErrorResponseDTO;
import com.microservices.profile.dto.error.ValidationErrorResponseDTO;
import com.microservices.profile.exceptions.AddressNotFoundException;
import com.microservices.profile.exceptions.DuplicateEmailException;
import com.microservices.profile.exceptions.UserNotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@GrpcAdvice
public class GRPCExpecptionHandler {

    @GrpcExceptionHandler(UserNotFoundException.class)
    public StatusRuntimeException handleUserNotFound(UserNotFoundException ex){

        return Status.NOT_FOUND
                .withDescription(ex.getMessage())
                .asRuntimeException();
    }

    @GrpcExceptionHandler(DuplicateEmailException.class)
    public StatusRuntimeException handleDuplicateEmail(DuplicateEmailException ex){

        return Status.ALREADY_EXISTS
                .withDescription(ex.getMessage())
                .asRuntimeException();
    }

    @GrpcExceptionHandler(DataIntegrityViolationException.class)
    public StatusRuntimeException handlesDataIntergity(DataIntegrityViolationException ex){
        String message = "Database constraint violation";

        Throwable rootCause = ex.getMostSpecificCause();
        String rootMessage = (rootCause != null ? rootCause.getMessage() : "");

        if (rootMessage.contains("uk_user_address_type")) {
            message = "Address type already exists for this user";
        }
        if (rootMessage.contains("uk_user_email")) {
            message = "Email already exists";
        }

        return Status.ALREADY_EXISTS
                .withDescription(message)
                .asRuntimeException();
    }

    @GrpcExceptionHandler(AddressNotFoundException.class)
    public StatusRuntimeException handleAddressNotFound(AddressNotFoundException ex){
        return Status.NOT_FOUND
                .withDescription(ex.getMessage())
                .asRuntimeException();
    }


//    @GrpcExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ValidationErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
//                                                                       HttpServletRequest req){
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(
//                error -> errors.put(error.getField(), error.getDefaultMessage())
//        );
//        return buildValidationErrorResponse(HttpStatus.BAD_REQUEST,errors, req.getRequestURI());
//    }

//    @GrpcExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ErrorResponseDTO> handleInvalidEnum(HttpMessageNotReadableException ex,
//                                                              HttpServletRequest req){
//
//        Throwable cause = ex.getCause();
//        if(cause instanceof InvalidFormatException invalidFormatException){
//            Class<?> targetType =
//                    invalidFormatException.getTargetType();
//            if(targetType.isEnum()){
//                return buildErrorResponse(
//                        HttpStatus.BAD_REQUEST,
//                        "Invalid enum value provided",
//                        req.getRequestURI()
//                );
//            }
//        }
//        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", req.getRequestURI());
//    }

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
