package com.microservices.gateway.DTOS.oauth2;

import java.util.List;

public record OAuth2ClientRegistrationResponseDTO(
        String clientId,
        String clientSecret,
        String clientName,
        List<String> redirectUris,
        List<String> scopes,
        String clientType,
        List<String> grantTypes,
        long createdAtEpochSeconds
) {}
