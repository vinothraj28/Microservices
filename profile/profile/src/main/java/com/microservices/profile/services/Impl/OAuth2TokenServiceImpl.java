package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.OAuth2ValidationException;
import com.microservices.profile.models.entities.RefreshToken;
import com.microservices.profile.models.entities.RevokedToken;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.repository.RefreshTokenRepository;
import com.microservices.profile.repository.RevokedTokenRepository;
import com.microservices.profile.services.OAuth2AuthorizationCodeService;
import com.microservices.profile.services.OAuth2TokenService;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.spi.managers.UserManager;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

    private final TokenService tokenService;
    private final UserManager userManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final OAuth2AuthorizationCodeService oAuth2AuthorizationCodeService;

    @Value("${jwt.expiration-minutes:60}")
    private long accessTokenExpirationMinutes;

    public OAuth2TokenServiceImpl(
            TokenService tokenService,
            UserManager userManager,
            RefreshTokenRepository refreshTokenRepository,
            RevokedTokenRepository revokedTokenRepository,
            OAuth2AuthorizationCodeService oAuth2AuthorizationCodeService
    ) {
        this.tokenService = tokenService;
        this.userManager = userManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.oAuth2AuthorizationCodeService = oAuth2AuthorizationCodeService;
    }

    @Override
    @Transactional
    public TokenResult issueTokens(UserProfile userProfile, String clientId, String scope) {
        String accessToken = tokenService.generateToken(userProfile);
        String refreshToken = tokenService.generateRefreshToken(userProfile);
        String normalizedScope = normalizeScope(scope);
        saveRefreshToken(userProfile.getUserId(), clientId, normalizedScope, refreshToken, null);

        return new TokenResult(
                accessToken,
                refreshToken,
                "Bearer",
                accessTokenExpirationMinutes * 60,
                normalizedScope
        );
    }

    @Override
    @Transactional
    public TokenResult exchangeAuthorizationCode(
            String authorizationCode,
            String clientId,
            String redirectUri,
            String codeVerifier
    ) {
        OAuth2AuthorizationCodeService.AuthorizationCodeContext context =
                oAuth2AuthorizationCodeService.consumeAuthorizationCode(
                        authorizationCode,
                        clientId,
                        redirectUri,
                        codeVerifier
                );

        UserProfile userProfile = userManager.getUserByEmailAddress(context.email())
                .orElseThrow(() -> new OAuth2ValidationException("User not found for authorization code"));

        return issueTokens(userProfile, clientId, context.scope());
    }

    @Override
    @Transactional
    public TokenResult refreshTokens(String refreshToken, String clientId, String requestedScope) {
        Claims claims = tokenService.extractClaims(refreshToken);
        Object type = claims.get("type");
        if (type == null || !"refresh".equals(type)) {
            throw new OAuth2ValidationException("Token is not a refresh token");
        }

        UUID userId = parseUserId(claims.getSubject());
        String jti = tokenService.extractJti(refreshToken);
        RefreshToken storedRefreshToken = refreshTokenRepository.findByJtiAndUserIdAndClientId(jti, userId, clientId)
                .orElseThrow(() -> new OAuth2ValidationException("Refresh token not found for client"));

        if (!storedRefreshToken.isValid()) {
            throw new OAuth2ValidationException("Refresh token expired or revoked");
        }

        String scopeToIssue = resolveRefreshScope(storedRefreshToken.getScope(), requestedScope);
        UserProfile userProfile = userManager.getUserById(userId)
                .orElseThrow(() -> new OAuth2ValidationException("User not found"));

        storedRefreshToken.revoke();
        refreshTokenRepository.save(storedRefreshToken);

        String newAccessToken = tokenService.generateToken(userProfile);
        String newRefreshToken = tokenService.generateRefreshToken(userProfile);
        saveRefreshToken(userId, clientId, scopeToIssue, newRefreshToken, storedRefreshToken.getTokenId());

        return new TokenResult(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                accessTokenExpirationMinutes * 60,
                scopeToIssue
        );
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String refreshToken, String reason) {
        Claims claims = tokenService.extractClaims(refreshToken);
        UUID userId = parseUserId(claims.getSubject());
        String jti = tokenService.extractJti(refreshToken);

        RefreshToken token = refreshTokenRepository.findByJtiAndUserId(jti, userId)
                .orElseThrow(() -> new OAuth2ValidationException("Refresh token not found"));
        token.revoke();
        refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public void revokeAccessToken(String accessToken, String clientId, String reason) {
        Claims claims = tokenService.extractClaims(accessToken);
        String jti = tokenService.extractJti(accessToken);
        if (revokedTokenRepository.findByJti(jti).isPresent()) {
            return;
        }

        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setJti(jti);
        revokedToken.setUserId(parseUserId(claims.getSubject()));
        revokedToken.setClientId(clientId);
        revokedToken.setTokenType("access_token");
        revokedToken.setExpiresAt(toLocalDateTime(claims.getExpiration().getTime()));
        revokedToken.setRevocationReason(isBlank(reason) ? "manual_revoke" : reason);
        revokedTokenRepository.save(revokedToken);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAccessTokenRevoked(String accessToken) {
        Claims claims = tokenService.extractClaims(accessToken);
        String jti = claims.getId();
        return revokedTokenRepository.existsByJtiAndExpiresAtAfter(jti, LocalDateTime.now());
    }

    private void saveRefreshToken(
            UUID userId,
            String clientId,
            String scope,
            String refreshToken,
            UUID parentTokenId
    ) {
        Claims refreshClaims = tokenService.extractClaims(refreshToken);
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setClientId(clientId);
        token.setJti(refreshClaims.getId());
        token.setToken(refreshToken);
        token.setScope(scope);
        token.setParentTokenId(parentTokenId);
        token.setExpirationTime(toLocalDateTime(refreshClaims.getExpiration().getTime()));
        token.setRevoked(false);
        refreshTokenRepository.save(token);
    }

    private UUID parseUserId(String subject) {
        try {
            return UUID.fromString(subject);
        } catch (IllegalArgumentException ex) {
            throw new OAuth2ValidationException("Invalid token subject", ex);
        }
    }

    private String resolveRefreshScope(String originalScope, String requestedScope) {
        if (isBlank(requestedScope)) {
            return normalizeScope(originalScope);
        }

        Set<String> original = parseScope(originalScope);
        Set<String> requested = parseScope(requestedScope);

        if (!original.containsAll(requested)) {
            throw new OAuth2ValidationException("requested scope cannot exceed original token scope");
        }
        return String.join(" ", requested);
    }

    private String normalizeScope(String scope) {
        if (isBlank(scope)) {
            return "";
        }
        return String.join(" ", parseScope(scope));
    }

    private Set<String> parseScope(String scope) {
        Set<String> values = new LinkedHashSet<>();
        if (isBlank(scope)) {
            return values;
        }
        Arrays.stream(scope.trim().split("\\s+"))
                .filter(s -> !s.isBlank())
                .forEach(values::add);
        return values;
    }

    private LocalDateTime toLocalDateTime(long epochMillis) {
        return LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
