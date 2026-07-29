package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.OAuth2ValidationException;
import com.microservices.profile.models.entities.OAuth2Client;
import com.microservices.profile.models.enums.ClientType;
import com.microservices.profile.models.enums.GrantType;
import com.microservices.profile.repository.OAuth2ClientRepository;
import com.microservices.profile.services.OAuth2ClientService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    private final OAuth2ClientRepository oAuth2ClientRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom secureRandom = new SecureRandom();

    public OAuth2ClientServiceImpl(OAuth2ClientRepository oAuth2ClientRepository) {
        this.oAuth2ClientRepository = oAuth2ClientRepository;
    }

    @Override
    @Transactional
    public RegisteredClient registerClient(
            String clientName,
            List<String> redirectUris,
            List<String> scopes,
            List<GrantType> grantTypes,
            ClientType clientType,
            String applicationType
    ) {
        if (isBlank(clientName)) {
            throw new OAuth2ValidationException("client_name is required");
        }
        if (redirectUris == null || redirectUris.isEmpty()) {
            throw new OAuth2ValidationException("At least one redirect URI is required");
        }
        if (scopes == null || scopes.isEmpty()) {
            throw new OAuth2ValidationException("At least one scope is required");
        }
        if (grantTypes == null || grantTypes.isEmpty()) {
            throw new OAuth2ValidationException("At least one grant type is required");
        }
        if (clientType == null) {
            throw new OAuth2ValidationException("client_type is required");
        }

        OAuth2Client client = new OAuth2Client();
        client.setClientId(generateUniqueClientId());
        client.setClientName(clientName.trim());
        client.setRedirectUris(redirectUris.stream().map(String::trim).toList());
        client.setAllowedScopes(scopes.stream().map(String::trim).toList());
        client.setGrantTypes(grantTypes);
        client.setClientType(clientType);
        client.setApplicationType(applicationType);
        client.setActive(true);

        String plainClientSecret = null;
        if (client.isConfidential()) {
            plainClientSecret = generateClientSecret();
            client.setClientSecretHash(passwordEncoder.encode(plainClientSecret));
        }

        OAuth2Client saved = oAuth2ClientRepository.save(client);
        return new RegisteredClient(saved.getClientId(), plainClientSecret, saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Client getActiveClient(String clientId) {
        return oAuth2ClientRepository.findByClientIdAndIsActiveTrue(clientId)
                .orElseThrow(() -> new OAuth2ValidationException("Invalid or inactive client"));
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Client validateClientCredentials(String clientId, String clientSecret) {
        OAuth2Client client = getActiveClient(clientId);

        if (client.isConfidential()) {
            if (isBlank(clientSecret)) {
                throw new OAuth2ValidationException("client_secret is required for confidential clients");
            }
            if (!passwordEncoder.matches(clientSecret, client.getClientSecretHash())) {
                throw new OAuth2ValidationException("Invalid client credentials");
            }
        }

        return client;
    }

    @Override
    public void validateRedirectUri(OAuth2Client client, String redirectUri) {
        if (isBlank(redirectUri)) {
            throw new OAuth2ValidationException("redirect_uri is required");
        }
        if (!client.hasRedirectUri(redirectUri)) {
            throw new OAuth2ValidationException("Invalid redirect_uri for client");
        }
    }

    @Override
    public Set<String> validateAndNormalizeScopes(OAuth2Client client, String scope) {
        if (isBlank(scope)) {
            return new LinkedHashSet<>(client.getAllowedScopes());
        }

        Set<String> requested = new LinkedHashSet<>();
        for (String requestedScope : scope.trim().split("\\s+")) {
            if (!client.isAllowedScope(requestedScope)) {
                throw new OAuth2ValidationException("Invalid scope requested: " + requestedScope);
            }
            requested.add(requestedScope);
        }
        return requested;
    }

    private String generateUniqueClientId() {
        String clientId = UUID.randomUUID().toString();
        while (oAuth2ClientRepository.existsByClientId(clientId)) {
            clientId = UUID.randomUUID().toString();
        }
        return clientId;
    }

    private String generateClientSecret() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
