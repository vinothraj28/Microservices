package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.OAuth2ValidationException;
import com.microservices.profile.models.entities.AuthorizationCode;
import com.microservices.profile.repository.AuthorizationCodeRepository;
import com.microservices.profile.services.OAuth2AuthorizationCodeService;
import com.microservices.profile.services.PKCEValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OAuth2AuthorizationCodeServiceImpl implements OAuth2AuthorizationCodeService {

    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final PKCEValidator pkceValidator;

    @Value("${oauth2.authorization-code-ttl-seconds:600}")
    private long authorizationCodeTtlSeconds;

    public OAuth2AuthorizationCodeServiceImpl(
            AuthorizationCodeRepository authorizationCodeRepository,
            PKCEValidator pkceValidator
    ) {
        this.authorizationCodeRepository = authorizationCodeRepository;
        this.pkceValidator = pkceValidator;
    }

    @Override
    @Transactional
    public String createAuthorizationCode(
            String email,
            String clientId,
            String redirectUri,
            String scope,
            String codeChallenge,
            String codeChallengeMethod,
            String nonce
    ) {
        if (email == null) {
            throw new OAuth2ValidationException("Authenticated user is required");
        }
        if (isBlank(clientId)) {
            throw new OAuth2ValidationException("client_id is required");
        }
        if (isBlank(redirectUri)) {
            throw new OAuth2ValidationException("redirect_uri is required");
        }

        pkceValidator.validateRequest(codeChallenge, codeChallengeMethod, true);

        AuthorizationCode authorizationCode = new AuthorizationCode();
        authorizationCode.setCode(UUID.randomUUID().toString().replace("-", ""));
        authorizationCode.setEmail(email);
        authorizationCode.setClientId(clientId);
        authorizationCode.setRedirectUri(redirectUri);
        authorizationCode.setScope(scope);
        authorizationCode.setCodeChallenge(codeChallenge);
        authorizationCode.setCodeChallengeMethod(codeChallengeMethod);
        authorizationCode.setNonce(nonce);
        authorizationCode.setExpiresAt(LocalDateTime.now().plusSeconds(authorizationCodeTtlSeconds));
        authorizationCode.setUsed(false);

        return authorizationCodeRepository.save(authorizationCode).getCode();
    }

    @Override
    @Transactional
    public AuthorizationCodeContext consumeAuthorizationCode(
            String code,
            String clientId,
            String redirectUri,
            String codeVerifier
    ) {
        if (isBlank(code)) {
            throw new OAuth2ValidationException("authorization code is required");
        }

        AuthorizationCode authorizationCode = authorizationCodeRepository.findLockedByCode(code)
                .orElseThrow(() -> new OAuth2ValidationException("Invalid authorization code"));

        if (!authorizationCode.isValid()) {
            throw new OAuth2ValidationException("Authorization code expired or already used");
        }

        if (!authorizationCode.getClientId().equals(clientId)) {
            throw new OAuth2ValidationException("authorization code does not belong to client");
        }

        if (!authorizationCode.getRedirectUri().equals(redirectUri)) {
            throw new OAuth2ValidationException("redirect_uri mismatch");
        }

        if (!pkceValidator.verifyCodeVerifier(
                codeVerifier,
                authorizationCode.getCodeChallenge(),
                authorizationCode.getCodeChallengeMethod()
        )) {
            throw new OAuth2ValidationException("Invalid PKCE code_verifier");
        }

        authorizationCode.markAsUsed();
        authorizationCodeRepository.save(authorizationCode);

        return new AuthorizationCodeContext(
                authorizationCode.getEmail(),
                authorizationCode.getClientId(),
                authorizationCode.getScope(),
                authorizationCode.getNonce()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
