package com.microservices.profile.dto.mfa;

public record MFASetupResponse(
        String qrCodeUrl
) {
}
