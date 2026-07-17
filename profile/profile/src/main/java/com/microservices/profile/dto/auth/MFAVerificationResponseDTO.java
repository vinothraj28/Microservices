package com.microservices.profile.dto.auth;

/**
 * Application Layer DTO for MFA verification responses.
 * <p>
 * Returned after successful MFA code verification.
 * Contains the final JWT token that grants the user authenticated access.
 *
 * @param accessToken  The final JWT token for the authenticated user after MFA verification
 * @param refreshToken
 */
public record MFAVerificationResponseDTO(
        String accessToken,
        String refreshToken) {
    /**
     * Factory method for successful MFA verification.
     * @param accessToken The JWT token after successful MFA verification
     * @return MFAVerificationResponseDTO with token
     */
    public static MFAVerificationResponseDTO success(String accessToken, String refreshToken) {
        return new MFAVerificationResponseDTO(accessToken, refreshToken);
    }
}

