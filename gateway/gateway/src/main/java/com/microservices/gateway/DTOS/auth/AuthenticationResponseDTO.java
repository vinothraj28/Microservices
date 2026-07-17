package com.microservices.gateway.DTOS.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Authentication response DTO for gateway endpoints.
 * Maps to gRPC AuthenticationResponse.
 *
 * Contains either:
 * - accessToken (if MFA not required)
 * - mfaChallengeToken (if MFA required)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthenticationResponseDTO(
        String accessToken,
        String mfaChallengeToken,
        String refreshToken,
        boolean mfaRequired
) {
    /**
     * Factory method for successful authentication without MFA.
     */
    public static AuthenticationResponseDTO successWithoutMfa(String accessToken) {
        return new AuthenticationResponseDTO(accessToken, null, null,false);
    }

    /**
     * Factory method for authentication requiring MFA verification.
     */
    public static AuthenticationResponseDTO mfaChallengeRequired(String mfaChallengeToken) {
        return new AuthenticationResponseDTO(null, mfaChallengeToken, null, true);
    }
}

