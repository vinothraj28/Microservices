package com.microservices.profile.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AuthorizationCode Entity
 *
 * Represents an OAuth2 authorization code issued during the authorization code flow.
 * Implements PKCE (Proof Key for Code Exchange) to prevent authorization code interception attacks.
 *
 * LIFECYCLE:
 * 1. Created when user authorizes a client (after successful login)
 * 2. Used once to exchange for access/refresh tokens
 * 3. Marked as used after successful token exchange
 * 4. Expired after short TTL (default: 10 minutes)
 *
 * SECURITY:
 * - Single-use: Once exchanged, code is marked as 'used' and cannot be reused
 * - Short-lived: Expires after 10 minutes (configurable)
 * - PKCE: code_challenge stored with code, code_verifier validated during exchange
 * - Bound to client_id: Only the client that requested can exchange the code
 * - Bound to redirect_uri: Prevents authorization code interception via redirect
 *
 * PKCE (RFC 7636):
 * - code_challenge: SHA256(code_verifier) sent during /authorize
 * - code_verifier: Random string sent during /token to prove possession
 * - Prevents authorization code interception (MITM attacks)
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1">RFC 6749 - Authorization Code Flow</a>
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7636">RFC 7636 - PKCE</a>
 */
@Entity
@Table(name = "authorization_codes", indexes = {
        @Index(name = "idx_code", columnList = "code", unique = true),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_client_id", columnList = "client_id"),
        @Index(name = "idx_expires_at", columnList = "expires_at"),
        @Index(name = "idx_used", columnList = "used")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationCode implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The authorization code value (opaque to client)
     * Generated as secure random UUID
     */
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    /**
     * User who authorized this code
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Client that requested this authorization
     */
    @Column(name = "client_id", nullable = false, length = 36)
    private String clientId;

    /**
     * Redirect URI provided during authorization request
     * Must match exactly during token exchange (security)
     */
    @Column(name = "redirect_uri", nullable = false, length = 500)
    private String redirectUri;

    /**
     * Space-separated list of scopes authorized by the user
     */
    @Column(name = "scope", length = 500)
    private String scope;

    /**
     * PKCE code challenge (SHA256 hash of code_verifier)
     * Validated during token exchange
     */
    @Column(name = "code_challenge", nullable = false, length = 255)
    private String codeChallenge;

    /**
     * PKCE code challenge method: "S256" (SHA-256) or "plain"
     * S256 is recommended for security
     */
    @Column(name = "code_challenge_method", nullable = false, length = 10)
    private String codeChallengeMethod;

    /**
     * OpenID Connect nonce (optional)
     * Included in ID token to prevent replay attacks
     */
    @Column(name = "nonce", length = 255)
    private String nonce;

    /**
     * When this authorization code was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When this authorization code expires
     * Default: 10 minutes from creation
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Whether this code has been used (single-use enforcement)
     */
    @Column(name = "used", nullable = false)
    private boolean used = false;

    /**
     * When this code was used (null if not yet used)
     */
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    /**
     * Check if this authorization code is valid for exchange
     *
     * @return true if not expired, not used, and within validity window
     */
    public boolean isValid() {
        return !used && LocalDateTime.now().isBefore(expiresAt);
    }

    /**
     * Mark this code as used (prevents reuse)
     */
    public void markAsUsed() {
        this.used = true;
        this.usedAt = LocalDateTime.now();
    }

    /**
     * Check if this code is expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
