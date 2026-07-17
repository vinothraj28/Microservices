package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.models.entities.RefreshToken;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.repository.RefreshTokenRepository;
import com.microservices.profile.services.RefreshTokenService;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.spi.managers.UserManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of RefreshTokenService.
 *
 * Manages the complete refresh token lifecycle:
 * 1. Storing refresh tokens after authentication
 * 2. Validating refresh tokens for token refresh
 * 3. Revoking tokens during logout
 * 4. Cleaning up expired tokens
 *
 * ARCHITECTURE:
 * - Uses database storage for refresh tokens (enables revocation)
 * - Validates both JWT signature and database state
 * - Coordinates with TokenService for JWT operations
 * - Coordinates with UserManager for user lookups
 *
 * SECURITY:
 * - Refresh tokens are validated against database state
 * - Revoked tokens cannot be used even if JWT is valid
 * - Each refresh generates new tokens (prevents token reuse)
 * - Tokens are linked to users to prevent cross-user usage
 */
@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final UserManager userManager;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            TokenService tokenService,
            UserManager userManager
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
        this.userManager = userManager;
    }

    /**
     * Save a refresh token to the database.
     *
     * Extracts the JTI claim from the JWT and stores the token with
     * its expiration time for later validation.
     *
     * @param userId The user ID
     * @param refreshToken The refresh token JWT string
     */
    @Override
    @Transactional
    public void saveRefreshToken(UUID userId, String refreshToken) {
        log.debug("Saving refresh token for user: {}", userId);

        try {
            // Extract JWT claims to get JTI and expiration
            Claims claims = tokenService.extractClaims(refreshToken);
            String jti = tokenService.extractJti(refreshToken);

            // Parse expiration time
            long expirationMs = claims.getExpiration().getTime();
            LocalDateTime expirationTime = Instant.ofEpochMilli(expirationMs)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();

            // Create and save the refresh token entity
            RefreshToken token = new RefreshToken();
            token.setUserId(userId);
            token.setJti(jti);
            token.setToken(refreshToken);
            token.setExpirationTime(expirationTime);
            token.setRevoked(false);

            refreshTokenRepository.save(token);
            log.info("Refresh token saved successfully for user: {}", userId);

        } catch (JwtException e) {
            log.error("Failed to extract claims from refresh token", e);
            throw new InvalidCredentialsException("Invalid refresh token format", e);
        }
    }

    /**
     * Validate and refresh an access token using a refresh token.
     *
     * FLOW:
     * 1. Parse and validate refresh token JWT
     * 2. Extract user ID and JTI from token
     * 3. Verify token exists in database and is not revoked
     * 4. Verify token is not expired
     * 5. Generate new access and refresh tokens
     * 6. Update database with new refresh token
     *
     * @param refreshToken The refresh token JWT string
     * @return RefreshTokenResult with new access and refresh tokens
     * @throws InvalidCredentialsException if token is invalid or revoked
     */
    @Override
    @Transactional
    public RefreshTokenResult refreshAccessToken(String refreshToken) {
        log.debug("Attempting to refresh access token");

        try {
            // Step 1: Parse and validate JWT
            Claims claims = tokenService.extractClaims(refreshToken);

            // Verify this is a refresh token (not an access token)
            Object tokenType = claims.get("type");
            if (tokenType == null || !tokenType.equals("refresh")) {
                log.warn("Refresh attempt with invalid token type");
                throw new InvalidCredentialsException("Token is not a valid refresh token");
            }

            // Step 2: Extract user ID and JTI
            String userId = claims.getSubject();
            String jti = tokenService.extractJti(refreshToken);

            UUID userUUID;
            try {
                userUUID = UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid user ID in refresh token: {}", userId);
                throw new InvalidCredentialsException("Invalid refresh token");
            }

            // Step 3: Verify token exists in database and is not revoked
            Optional<RefreshToken> storedToken = refreshTokenRepository.findByJtiAndUserId(jti, userUUID);
            if (storedToken.isEmpty()) {
                log.warn("Refresh token not found in database for JTI: {}", jti);
                throw new InvalidCredentialsException("Refresh token has been revoked or is invalid");
            }

            RefreshToken token = storedToken.get();

            // Step 4: Verify token is not expired
            if (!token.isValid()) {
                log.warn("Refresh token is expired or revoked for user: {}", userId);
                throw new InvalidCredentialsException("Refresh token has expired");
            }

            // Step 5: Load user and generate new tokens
            Optional<UserProfile> userOptional = userManager.getUserById(userUUID);
            if (userOptional.isEmpty()) {
                log.warn("User not found for ID: {}", userId);
                throw new InvalidCredentialsException("User not found");
            }

            UserProfile user = userOptional.get();

            // Generate new tokens
            String newAccessToken = tokenService.generateToken(user);
            String newRefreshToken = tokenService.generateRefreshToken(user);

            // Step 6: Revoke old token and save new one
            log.debug("Revoking old refresh token and storing new one for user: {}", userId);
            token.revoke(); // Revoke the old token
            refreshTokenRepository.save(token);
            saveRefreshToken(userUUID, newRefreshToken);

            log.info("Access token refreshed successfully for user: {}", userId);

            return new RefreshTokenResult(newAccessToken, newRefreshToken);

        } catch (JwtException e) {
            log.warn("JWT validation failed during token refresh", e);
            throw new InvalidCredentialsException("Invalid or expired refresh token", e);
        }
    }

    /**
     * Revoke a refresh token (used during logout).
     *
     * Marks the token as revoked in the database so it cannot be used again,
     * even if the JWT signature is valid.
     *
     * @param refreshToken The refresh token JWT string
     */
    @Override
    @Transactional
    public void revokeRefreshToken(String refreshToken) {
        log.debug("Revoking refresh token");

        try {
            String jti = tokenService.extractJti(refreshToken);
            refreshTokenRepository.revokeByJti(jti);
            log.info("Refresh token revoked successfully for JTI: {}", jti);
        } catch (JwtException e) {
            log.warn("Failed to extract JTI from refresh token during revocation", e);
            // Log but don't throw - logout should be forgiving
        }
    }

    /**
     * Revoke all refresh tokens for a user.
     *
     * Used when user changes password or needs to force logout all sessions.
     *
     * @param userId The user ID
     */
    @Override
    @Transactional
    public void revokeAllTokensForUser(UUID userId) {
        log.info("Revoking all refresh tokens for user: {}", userId);
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("All refresh tokens revoked for user: {}", userId);
    }
}

