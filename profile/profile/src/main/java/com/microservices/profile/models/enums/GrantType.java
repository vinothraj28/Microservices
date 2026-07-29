package com.microservices.profile.models.enums;

/**
 * OAuth2 Grant Types
 *
 * Defines the OAuth2 flows (grant types) that clients can use to obtain tokens.
 * Each grant type has different security and use-case implications.
 *
 * AUTHORIZATION_CODE (RECOMMENDED):
 * - Most secure flow
 * - User authorizes client via redirect
 * - Client exchanges authorization code for tokens
 * - Supports refresh tokens
 * - Use with PKCE for public clients
 *
 * REFRESH_TOKEN:
 * - Used to obtain new access tokens without re-authentication
 * - Requires a valid refresh token
 * - Supports token rotation
 *
 * CLIENT_CREDENTIALS:
 * - Machine-to-machine authentication (no user context)
 * - Client authenticates with client_secret
 * - No refresh token issued
 * - Use for backend service-to-service calls
 *
 * PASSWORD (DEPRECATED):
 * - User provides username/password directly to client
 * - NOT RECOMMENDED - violates OAuth2 principles
 * - Only use for legacy migration or first-party highly trusted apps
 *
 * IMPLICIT (DEPRECATED):
 * - Access token returned directly from /authorize (no code exchange)
 * - DEPRECATED due to security risks
 * - Use authorization_code with PKCE instead
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-1.3">RFC 6749 - Grant Types</a>
 * @see <a href="https://oauth.net/2/grant-types/">OAuth 2.0 Grant Types</a>
 */
public enum GrantType {
    /**
     * Authorization Code Flow
     * Most secure, supports PKCE
     */
    AUTHORIZATION_CODE,

    /**
     * Refresh Token Flow
     * Obtain new access token using refresh token
     */
    REFRESH_TOKEN,

    /**
     * Client Credentials Flow
     * Machine-to-machine, no user context
     */
    CLIENT_CREDENTIALS,

    /**
     * Resource Owner Password Credentials Flow
     * DEPRECATED - user provides password directly to client
     */
    PASSWORD,

    /**
     * Implicit Flow
     * DEPRECATED - use authorization_code with PKCE instead
     */
    IMPLICIT
}
