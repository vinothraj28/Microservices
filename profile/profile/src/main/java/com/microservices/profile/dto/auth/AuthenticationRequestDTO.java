package com.microservices.profile.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Application Layer DTO for authentication requests.
 * Represents a user's initial login attempt with credentials.
 *
 * @param email User's email address
 * @param password User's password
 */
public record AuthenticationRequestDTO(
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password
) {}

