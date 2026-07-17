package com.microservices.profile.models.entities;

import com.microservices.profile.models.audits.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * RefreshToken Entity
 *
 * Stores refresh tokens in the database to enable:
 * 1. Token revocation (logout functionality)
 * 2. Token blacklisting
 * 3. Token expiration management
 * 4. Session tracking
 *
 * DESIGN:
 * - Stores the JTI (JWT ID) from the refresh token claim
 * - Links to UserProfile via userId
 * - Tracks creation and expiration times
 * - Maintains revocation status for logout
 *
 * LIFECYCLE:
 * - Created when user authenticates
 * - Validated when user requests token refresh
 * - Revoked when user logs out
 * - Automatically expired based on expirationTime
 */
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_jti", columnList = "jti"),
        @Index(name = "idx_revoked", columnList = "revoked")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tokenId;

    /**
     * User who owns this refresh token
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * JWT ID (jti claim) - unique identifier for this token from the JWT itself
     * Used to match the refresh token with the JWT claim
     */
    @Column(name = "jti", nullable = false, unique = true, length = 255)
    private String jti;

    /**
     * Refresh token string (we store the token itself for validation)
     * In production, consider storing only a hash of the token
     */
    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    /**
     * When this refresh token was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When this refresh token expires
     */
    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    /**
     * Whether this token has been revoked (e.g., by logout)
     */
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Override
    public UUID getId() {
        return tokenId;
    }

    /**
     * Check if this refresh token is valid for use
     *
     * @return true if token is not revoked and not expired
     */
    public boolean isValid() {
        return !revoked && LocalDateTime.now().isBefore(expirationTime);
    }

    /**
     * Mark this token as revoked (used during logout)
     */
    public void revoke() {
        this.revoked = true;
    }
}

