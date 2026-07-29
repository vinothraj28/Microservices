package com.microservices.profile.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * RevokedToken Entity
 *
 * Tracks revoked access tokens for logout and security purposes.
 * Since access tokens are stateless (JWT), we need to track revocations in the database.
 *
 * DESIGN DECISIONS:
 * - Store only the JTI (JWT ID) claim, not the full token
 * - Automatically clean up expired tokens (revoked_at + token TTL)
 * - Index on jti for fast lookup during token validation
 * - Include expires_at to enable cleanup jobs
 *
 * SECURITY:
 * - Prevents revoked tokens from being used
 * - Supports immediate logout (revoke all user tokens)
 * - Enables security incident response (revoke compromised tokens)
 *
 * CLEANUP:
 * - Tokens can be deleted from this table after expires_at (no longer relevant)
 * - Recommended: Daily cleanup job to remove expired revocations
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7009">RFC 7009 - Token Revocation</a>
 */
@Entity
@Table(name = "revoked_tokens", indexes = {
        @Index(name = "idx_jti", columnList = "jti", unique = true),
        @Index(name = "idx_expires_at", columnList = "expires_at"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_client_id", columnList = "client_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevokedToken implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * JWT ID (jti claim) from the revoked token
     * Used to check if a token has been revoked
     */
    @Column(name = "jti", nullable = false, unique = true, length = 255)
    private String jti;

    /**
     * User who owned this token (for audit and bulk revocation)
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Client that this token was issued to (for audit)
     */
    @Column(name = "client_id", length = 36)
    private String clientId;

    /**
     * Token type: "access_token" or "refresh_token"
     */
    @Column(name = "token_type", nullable = false, length = 20)
    private String tokenType;

    /**
     * When this token was revoked
     */
    @CreationTimestamp
    @Column(name = "revoked_at", nullable = false, updatable = false)
    private LocalDateTime revokedAt;

    /**
     * When this token expires (from original exp claim)
     * Used for cleanup - revocations can be deleted after this time
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Reason for revocation (for audit)
     * Examples: "user_logout", "admin_revoke", "security_incident", "token_rotation"
     */
    @Column(name = "revocation_reason", length = 100)
    private String revocationReason;

    /**
     * Check if this revocation record is still relevant
     * Once the token expires naturally, the revocation is no longer needed
     */
    public boolean isRelevant() {
        return LocalDateTime.now().isBefore(expiresAt);
    }
}
