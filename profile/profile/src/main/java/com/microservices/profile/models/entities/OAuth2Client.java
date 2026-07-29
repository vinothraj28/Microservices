package com.microservices.profile.models.entities;

import com.microservices.profile.models.audits.AuditableEntity;
import com.microservices.profile.models.enums.ClientType;
import com.microservices.profile.models.enums.GrantType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

/**
 * OAuth2Client Entity
 *
 * Represents an OAuth2 client application that can request authorization on behalf of users.
 * Implements OAuth2/OIDC client registration (RFC 7591).
 *
 * DESIGN DECISIONS:
 * - client_id: UUID for unpredictability (security)
 * - client_secret: Stored as BCrypt hash, only for CONFIDENTIAL clients
 * - redirect_uris: JSON array for flexibility, validated on authorization
 * - scopes: Whitelist of allowed scopes this client can request
 * - grant_types: Restricts which OAuth2 flows this client can use
 * - is_confidential: PUBLIC (SPAs, mobile) vs CONFIDENTIAL (server-side apps)
 *
 * SECURITY:
 * - client_secret_hash is BCrypt hashed, never stored in plain text
 * - redirect_uris are strictly validated to prevent open redirects
 * - is_active flag allows disabling compromised clients without deletion
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-2">RFC 6749 - Client Registration</a>
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7591">RFC 7591 - Dynamic Client Registration</a>
 */
@Entity
@Table(name = "oauth2_clients", indexes = {
        @Index(name = "idx_client_id", columnList = "client_id", unique = true),
        @Index(name = "idx_client_name", columnList = "client_name"),
        @Index(name = "idx_is_active", columnList = "is_active")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Client extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * OAuth2 client_id - unique identifier for this client
     * Generated as UUID for unpredictability
     */
    @Column(name = "client_id", nullable = false, unique = true, length = 36)
    private String clientId;

    /**
     * BCrypt hash of client_secret (for CONFIDENTIAL clients only)
     * Never stored in plain text. Empty/null for PUBLIC clients.
     */
    @Column(name = "client_secret_hash", length = 255)
    private String clientSecretHash;

    /**
     * Human-readable name of the client application
     * Displayed to users during authorization
     */
    @Column(name = "client_name", nullable = false, length = 255)
    private String clientName;

    /**
     * List of allowed redirect URIs for this client
     * Validated during authorization to prevent open redirect attacks
     * Stored as JSON array for flexibility
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "redirect_uris", nullable = false, columnDefinition = "jsonb")
    private List<String> redirectUris;

    /**
     * List of scopes this client is allowed to request
     * Whitelist enforced during authorization
     * Examples: ["user:read", "user:write", "admin:all"]
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "allowed_scopes", nullable = false, columnDefinition = "jsonb")
    private List<String> allowedScopes;

    /**
     * List of grant types this client can use
     * Examples: AUTHORIZATION_CODE, REFRESH_TOKEN, CLIENT_CREDENTIALS
     */
    @ElementCollection(targetClass = GrantType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "oauth2_client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type")
    @Enumerated(EnumType.STRING)
    private List<GrantType> grantTypes;

    /**
     * Client type: PUBLIC or CONFIDENTIAL
     * PUBLIC: Cannot securely store secrets (SPAs, mobile apps) - requires PKCE
     * CONFIDENTIAL: Can securely store secrets (server-side apps)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, length = 20)
    private ClientType clientType;

    /**
     * Application type: web, native, spa
     * Used for UI/UX decisions in authorization flow
     */
    @Column(name = "application_type", length = 20)
    private String applicationType;

    /**
     * URL of the client's logo (displayed during authorization)
     */
    @Column(name = "logo_uri", length = 500)
    private String logoUri;

    /**
     * URL of the client's homepage
     */
    @Column(name = "client_uri", length = 500)
    private String clientUri;

    /**
     * URL of the client's privacy policy
     */
    @Column(name = "policy_uri", length = 500)
    private String policyUri;

    /**
     * URL of the client's terms of service
     */
    @Column(name = "tos_uri", length = 500)
    private String tosUri;

    /**
     * Whether this client is active
     * Disabled clients cannot obtain new authorizations
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * Check if this client is a PUBLIC client (cannot securely store secrets)
     * PUBLIC clients MUST use PKCE
     */
    public boolean isPublic() {
        return clientType == ClientType.PUBLIC;
    }

    /**
     * Check if this client is a CONFIDENTIAL client (can securely store secrets)
     */
    public boolean isConfidential() {
        return clientType == ClientType.CONFIDENTIAL;
    }

    /**
     * Check if this client supports a specific grant type
     */
    public boolean supportsGrantType(GrantType grantType) {
        return grantTypes != null && grantTypes.contains(grantType);
    }

    /**
     * Check if a redirect URI is registered for this client
     */
    public boolean hasRedirectUri(String redirectUri) {
        return redirectUris != null && redirectUris.contains(redirectUri);
    }

    /**
     * Check if this client is allowed to request a specific scope
     */
    public boolean isAllowedScope(String scope) {
        return allowedScopes != null && allowedScopes.contains(scope);
    }
}
