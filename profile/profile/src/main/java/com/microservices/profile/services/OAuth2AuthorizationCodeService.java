package com.microservices.profile.services;

public interface OAuth2AuthorizationCodeService {

    String createAuthorizationCode(
            String email,
            String clientId,
            String redirectUri,
            String scope,
            String codeChallenge,
            String codeChallengeMethod,
            String nonce
    );

    AuthorizationCodeContext consumeAuthorizationCode(
            String code,
            String clientId,
            String redirectUri,
            String codeVerifier
    );

    record AuthorizationCodeContext(
            String email,
            String clientId,
            String scope,
            String nonce
    ) {}
}
