package com.microservices.profile.models.enums;

/**
 * OAuth2 Client Type
 *
 * Defines whether a client can securely store credentials (client_secret).
 * This determines the security requirements for the client.
 *
 * PUBLIC:
 * - Cannot securely store client_secret (e.g., SPAs, mobile apps, native apps)
 * - MUST use PKCE for authorization code flow
 * - client_secret is NOT issued or required
 * - Examples: Angular/React SPAs, iOS/Android apps, desktop apps
 *
 * CONFIDENTIAL:
 * - Can securely store client_secret (e.g., server-side web apps, backend services)
 * - MAY use PKCE (recommended but not required)
 * - client_secret is issued and required for token requests
 * - Examples: Java/Node.js backend apps, server-rendered web apps
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-2.1">RFC 6749 - Client Types</a>
 */
public enum ClientType {
    /**
     * Public client - cannot securely store secrets
     * PKCE is REQUIRED for security
     */
    PUBLIC,

    /**
     * Confidential client - can securely store secrets
     * PKCE is RECOMMENDED but not required
     */
    CONFIDENTIAL
}
