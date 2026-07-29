package com.microservices.profile.services;

import com.microservices.profile.models.entities.UserProfile;

public interface OAuth2TokenService {

    TokenResult issueTokens(UserProfile userProfile, String clientId, String scope);

    TokenResult exchangeAuthorizationCode(
            String authorizationCode,
            String clientId,
            String redirectUri,
            String codeVerifier
    );

    TokenResult refreshTokens(String refreshToken, String clientId, String requestedScope);

    void revokeRefreshToken(String refreshToken, String reason);

    void revokeAccessToken(String accessToken, String clientId, String reason);

    boolean isAccessTokenRevoked(String accessToken);

    record TokenResult(
            String accessToken,
            String refreshToken,
            String tokenType,
            long expiresIn,
            String scope
    ) {}
}
