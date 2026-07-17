package com.microservices.profile.repository;

import com.microservices.profile.models.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for RefreshToken entities.
 *
 * Provides database operations for refresh tokens:
 * - Storing new refresh tokens during authentication
 * - Retrieving tokens for validation during refresh
 * - Revoking tokens during logout
 * - Cleaning up expired tokens
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Find a refresh token by its JWT ID (jti claim)
     *
     * @param jti The JWT ID from the token
     * @return Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByJti(String jti);

    /**
     * Find all refresh tokens for a specific user
     *
     * @param userId The user ID
     * @return List of all refresh tokens for this user
     */
    Optional<RefreshToken> findByJtiAndUserId(String jti, UUID userId);

    /**
     * Revoke a refresh token by marking it as revoked
     *
     * @param jti The JWT ID of the token to revoke
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.jti = :jti")
    void revokeByJti(String jti);

    /**
     * Revoke all refresh tokens for a user (used during logout or password change)
     *
     * @param userId The user ID
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.userId = :userId")
    void revokeAllByUserId(UUID userId);

    /**
     * Delete all expired refresh tokens
     *
     * Used for maintenance/cleanup of old tokens
     *
     * @param now Current timestamp
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expirationTime < :now")
    void deleteExpiredTokens(LocalDateTime now);

    /**
     * Check if a refresh token is valid and not revoked
     *
     * @param jti The JWT ID
     * @param now Current timestamp
     * @return true if token exists, is not revoked, and has not expired
     */
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END " +
            "FROM RefreshToken rt " +
            "WHERE rt.jti = :jti AND rt.revoked = false AND rt.expirationTime > :now")
    boolean isTokenValid(String jti, LocalDateTime now);
}

