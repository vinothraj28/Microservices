package com.microservices.gateway.DTOS.mfa;

/**
 * MFA verification response DTO for gateway endpoints.
 * Maps to gRPC MfaVerificationResponse.
 *
 * Contains the final JWT access token after successful MFA verification.
 */
public record MfaVerificationResponseDTO(
        String accessToken,
        String refreshToken
) {}

