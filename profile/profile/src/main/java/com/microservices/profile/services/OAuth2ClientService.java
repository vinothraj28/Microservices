package com.microservices.profile.services;

import com.microservices.profile.models.entities.OAuth2Client;
import com.microservices.profile.models.enums.ClientType;
import com.microservices.profile.models.enums.GrantType;

import java.util.List;
import java.util.Set;

public interface OAuth2ClientService {

    RegisteredClient registerClient(
            String clientName,
            List<String> redirectUris,
            List<String> scopes,
            List<GrantType> grantTypes,
            ClientType clientType,
            String applicationType
    );

    OAuth2Client getActiveClient(String clientId);

    OAuth2Client validateClientCredentials(String clientId, String clientSecret);

    void validateRedirectUri(OAuth2Client client, String redirectUri);

    Set<String> validateAndNormalizeScopes(OAuth2Client client, String scope);

    record RegisteredClient(
            String clientId,
            String clientSecret,
            OAuth2Client client
    ) {}
}
