package com.microservices.gateway.DTOS.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Authentication request DTO for gateway endpoints.
 * Maps to gRPC AuthenticationRequest.
 */
public record AuthenticationRequestDTO(
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password
) {}

