package com.microservices.gateway.DTOS.mfa;

import jakarta.validation.constraints.NotBlank;

/**
 * MFA verification request DTO for gateway endpoints.
 * Maps to gRPC MfaVerificationRequest.
 */
public record MfaVerificationRequestDTO(
        @NotBlank(message = "MFA challenge token cannot be blank")
        String mfaChallengeToken,

        @NotBlank(message = "MFA code cannot be blank")
        String mfaCode
) {}

