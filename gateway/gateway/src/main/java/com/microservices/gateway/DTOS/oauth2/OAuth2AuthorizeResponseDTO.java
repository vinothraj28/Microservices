package com.microservices.gateway.DTOS.oauth2;

public record OAuth2AuthorizeResponseDTO(
        String authorizationCode,
        String state,
        String redirectUri,
        int expiresIn
) {

}
