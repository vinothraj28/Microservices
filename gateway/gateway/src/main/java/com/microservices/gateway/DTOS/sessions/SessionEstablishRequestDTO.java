package com.microservices.gateway.DTOS.sessions;

import jakarta.validation.constraints.NotBlank;

/**
 * Request to establish an OAuth2 session after successful login.
 * The Angular frontend sends this request after user authenticates
 * to create a server-side session for OAuth2 authorization flow.
 *
 * @param accessToken JWT access token from successful login
 */
public record SessionEstablishRequestDTO(
        @NotBlank(message = "Access token is required")
        String accessToken
) {
}
