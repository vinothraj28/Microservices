package com.microservices.gateway.DTOS.oauth2;

public record OAuth2IntrospectResponseDTO(
        boolean active,
        String scope,
        String clientId,
        String tokenType,
        long exp,
        long iat,
        String sub,
        String jti
) {}
