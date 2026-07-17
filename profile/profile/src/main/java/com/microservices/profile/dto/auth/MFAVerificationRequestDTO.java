package com.microservices.profile.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Application Layer DTO for MFA verification requests.
 *
 * This DTO is used when a user has already completed the initial authentication
 * but MFA was required. They use the mfaChallengeToken from the authentication response
 * along with their MFA code to complete the authentication process.
 *
 * @param mfaChallengeToken Token returned from the initial authentication step that contained MFA challenge
 * @param mfaCode The 6-digit (or variable length) MFA code from their authenticator app
 */
public record MFAVerificationRequestDTO(
        @NotBlank(message = "MFA challenge token cannot be blank")
        String mfaChallengeToken,

        @NotBlank(message = "MFA code cannot be blank")
        String mfaCode
) {}

