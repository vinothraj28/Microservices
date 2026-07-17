package com.microservices.profile.services;

import com.microservices.profile.models.entities.UserProfile;

import java.util.UUID;

/**
 * Service for managing refresh tokens.
 *
 * Handles:
 * 1. Creating and storing refresh tokens
 * 2. Validating refresh tokens
 * 3. Revoking refresh tokens (logout)
 * 4. Generating new access tokens from refresh tokens
 *
 * SECURITY CONSIDERATIONS:
 * - Refresh tokens are stored in database for revocation support
 * - Tokens can be revoked during logout
 * - Expired tokens are automatically cleaned up
 * - Refresh token reuse is prevented by revocation
 */
public interface RefreshTokenService {

    /**
     * Save a refresh token to the database.
     *
     * Called after successful authentication to store the refresh token
     * for later validation and refresh operations.
     *
     * @param userId The user ID
     * @param refreshToken The refresh token JWT string
     */
    void saveRefreshToken(UUID userId, String refreshToken);

    /**
     * Validate and refresh an access token using a refresh token.
     *
     * This method:
     * 1. Validates the refresh token is valid and not expired
     * 2. Verifies it matches the user ID in the token
     * 3. Generates a new access token
     * 4. Returns the new access and refresh tokens
     *
     * @param refreshToken The refresh token JWT string
     * @return A new pair of tokens (access + refresh)
     * @throws com.microservices.profile.exceptions.InvalidCredentialsException if token is invalid
     */
    RefreshTokenResult refreshAccessToken(String refreshToken);

    /**
     * Revoke a refresh token (used during logout).
     *
     * Marks the token as revoked in the database so it cannot be used again.
     *
     * @param refreshToken The refresh token JWT string
     */
    void revokeRefreshToken(String refreshToken);

    /**
     * Revoke all refresh tokens for a user.
     *
     * Used when user changes password or needs to force logout all sessions.
     *
     * @param userId The user ID
     */
    void revokeAllTokensForUser(UUID userId);

    /**
     * Result of a token refresh operation.
     *
     * Contains both new access and refresh tokens.
     */
    record RefreshTokenResult(
            String accessToken,
            String refreshToken
    ) {}
}

