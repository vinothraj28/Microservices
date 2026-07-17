package com.microservices.gateway.DTOS.auth;

public record RefreshTokenResponseDTO(String accessToken, String refreshToken) {
}
