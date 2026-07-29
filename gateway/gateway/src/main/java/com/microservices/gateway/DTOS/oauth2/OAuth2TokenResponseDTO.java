package com.microservices.gateway.DTOS.oauth2;

public record OAuth2TokenResponseDTO(
        String accessToken,
        String tokenType,
        int expiresIn,
        String refreshToken,
        String scope
) {}
