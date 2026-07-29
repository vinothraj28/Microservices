package com.microservices.gateway.DTOS.mfa;

import jakarta.validation.constraints.NotBlank;

/**
 * MFA verification request DTO for gateway endpoints.
 * Maps to gRPC MfaVerificationRequest.
 */
public record MfaVerificationRequestDTO(

        String mfaChallengeToken,

        @NotBlank(message = "MFA code cannot be blank")
        String mfaCode
) {
        public static MfaVerificationRequestDTO from(String mfaChallengeToken, String mfaCode) {
                return new MfaVerificationRequestDTO(mfaChallengeToken, mfaCode);
        }

}

