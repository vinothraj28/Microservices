package com.microservices.gateway.DTOS.oauth2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OAuth2ClientRegistrationRequestDTO(
        @NotBlank(message = "clientName is required")
        String clientName,

        @NotEmpty(message = "redirectUris are required")
        List<String> redirectUris,

        @NotEmpty(message = "scopes are required")
        List<String> scopes,

        @NotBlank(message = "clientType is required")
        String clientType,

        @NotEmpty(message = "grantTypes are required")
        List<String> grantTypes,

        String applicationType
) {}
