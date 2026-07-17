package com.microservices.profile.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Application Layer DTO for authentication responses.
 * Handles both successful authentication (token) and MFA challenge scenarios.
 *
 * This is a multi-purpose response that represents one of two authentication states:
 * 1. MFA Not Required: user receives accessToken immediately
 * 2. MFA Required: user receives mfaChallengeToken to be used for MFA verification
 *
 * The presence of mfaChallengeToken indicates the authentication is incomplete and
 * requires MFA verification.
 *
 * @param accessToken JWT token if authentication is complete and MFA is not required (nullable if MFA required)
 * @param mfaChallengeToken Short-lived token containing authentication state if MFA is required (nullable if MFA not required)
 * @param refreshToken Refresh token if authentication is complete and MFA is not required (nullable if MFA required)
 * @param mfaRequired Flag indicating whether MFA verification is required
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthenticationResponseDTO(
        String accessToken,
        String mfaChallengeToken,
        String refreshToken,
        boolean mfaRequired
) {
    /**
     * Factory method for successful authentication without MFA requirement.
     * @param accessToken The JWT token for the authenticated user
     * @param refreshToken The refresh token for the authenticated user
     * @return AuthenticationResponseDTO with token populated
     */
    public static AuthenticationResponseDTO successWithoutMfa(String accessToken, String refreshToken) {
        return new AuthenticationResponseDTO(accessToken, null, refreshToken, false);
    }

    /**
     * Factory method for authentication requiring MFA verification.
     * @param mfaChallengeToken Token containing authentication state for MFA verification
     * @return AuthenticationResponseDTO with MFA challenge token populated
     */
    public static AuthenticationResponseDTO mfaChallengeRequired(String mfaChallengeToken) {
        return new AuthenticationResponseDTO(null, mfaChallengeToken, null, true);
    }
}
