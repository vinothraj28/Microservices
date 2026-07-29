package com.microservices.gateway.DTOS.oauth2;

public record OAuth2RevokeResponseDTO(
        boolean success,
        String message
) {}
