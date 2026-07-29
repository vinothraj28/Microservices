package com.microservices.gateway.DTOS.oauth2;

import jakarta.validation.constraints.NotBlank;

public record OAuth2RevokeRequestDTO(
        String token,
        String tokenTypeHint,
        @NotBlank(message = "clientId is required")
        String clientId,
        String clientSecret
) {}
