package com.microservices.gateway.DTOS.oauth2;

import jakarta.validation.constraints.NotBlank;

public record OAuth2TokenRequestDTO(
        @NotBlank(message = "grantType is required")
        String grantType,
        String code,
        String redirectUri,
        String codeVerifier,
        String refreshToken,
        @NotBlank(message = "clientId is required")
        String clientId,
        String clientSecret,
        String scope
) {}
