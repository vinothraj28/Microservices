package com.microservices.profile.services;

import com.microservices.profile.dto.auth.AuthenticationRequestDTO;
import com.microservices.profile.dto.auth.AuthenticationResponseDTO;
import com.microservices.profile.dto.auth.MFAVerificationRequestDTO;
import com.microservices.profile.dto.auth.MFAVerificationResponseDTO;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.InvalidMfaChallengeException;
import com.microservices.profile.exceptions.MfaNotConfiguredException;
import com.microservices.profile.exceptions.UserNotFoundException;

/**
 * Application Service for Authentication Flow Orchestration.
 *
 * Manages the complete authentication lifecycle:
 * 1. AUTHENTICATE: Validate credentials and check if MFA is required
 * 2. VERIFY_MFA: Accept MFA challenge token + code and return final token
 * 3. LOGOUT: Revoke refresh tokens and invalidate session
 * 4. REFRESH_TOKEN: Use refresh token to get new access token
 *
 * This service acts as the orchestration layer between:
 * - REST/gRPC controllers (presentation layer)
 * - Domain services (credential validation, token generation, MFA verification)
 * - Domain entities (User, Credential aggregates)
 *
 * KEY ARCHITECTURAL PRINCIPLES:
 * - Single Responsibility: Orchestrates authentication flows only
 * - Clean Architecture: Coordinates domain services without domain logic
 * - Domain-Driven Design: Respects bounded contexts (Authentication is separate from User Management)
 * - Security: Implements step-wise verification with challenge tokens
 *
 * AUTHENTICATION FLOW:
 *
 * Case 1: User has MFA disabled
 * ┌──────────────┐
 * │   Authenticate
 * │   (email/pass)
 * └──────┬───────┘
 *        │
 *        ├─ Validate credentials (CredentialsManager)
 *        ├─ Check MFA status (CredentialsManager)
 *        ├─ MFA disabled: Generate JWT token
 *        └─ Return accessToken + refreshToken
 *
 * Case 2: User has MFA enabled
 * ┌──────────────────────┐
 * │   Authenticate
 * │   (email/pass)
 * └──────┬───────────────┘
 *        │
 *        ├─ Validate credentials (CredentialsManager)
 *        ├─ Check MFA status: MFA enabled
 *        ├─ Create MFA challenge token (captures user context)
 *        └─ Return mfaChallengeToken (awaiting MFA)
 *
 * ┌──────────────────────┐
 * │   Verify MFA
 * │   (challenge + code)
 * └──────┬───────────────┘
 *        │
 *        ├─ Decode & validate challenge token
 *        ├─ Verify MFA code (CredentialsManager)
 *        ├─ Generate final JWT token
 *        └─ Return accessToken + refreshToken
 *
 * ERROR SCENARIOS:
 * - InvalidCredentialsException: Email/password invalid
 * - MfaNotConfiguredException: Challenge token suggests MFA but no TOTP credential found
 * - InvalidMfaChallengeException: Challenge token invalid, expired, or tampered
 * - InvalidCredentialsException: MFA code verification failed
 *
 * @see com.microservices.profile.services.PasswordService Password validation
 * @see com.microservices.profile.services.TokenService JWT generation and verification
 * @see com.microservices.profile.services.RefreshTokenService Refresh token management
 * @see com.microservices.profile.spi.managers.CredentialsManager Credential validation and MFA lookup
 * @see com.microservices.profile.spi.managers.UserManager User lookup and retrieval
 */
public interface AuthenticationService {

    /**
     * Step 1: Authenticate user with email and password.
     *
     * This method performs initial authentication:
     * 1. Validates user email and password combination
     * 2. Checks if MFA is required for this user
     * 3. Returns either:
     *    a) Access token + refresh token (if MFA disabled) - authentication complete
     *    b) MFA challenge token (if MFA enabled) - requires Step 2
     *
     * SECURITY CONSIDERATIONS:
     * - Does NOT return user details (information disclosure prevention)
     * - Invalid credentials throw generic exception (timing attack prevention)
     * - MFA challenge token is short-lived and includes authentication context
     * - No audit log until MFA verified (prevents spam logs on failed MFA codes)
     *
     * @param authRequest Email and password credentials
     * @return AuthenticationResponseDTO containing either accessToken or mfaChallengeToken
     * @throws InvalidCredentialsException if email/password combination is invalid
     * @throws UserNotFoundException if user account does not exist
     *
     * @see AuthenticationResponseDTO for response structure
     */
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authRequest);

    /**
     * Step 2: Complete authentication by verifying MFA code.
     *
     * This method is called after receiving an MFA challenge token from Step 1.
     * It performs MFA verification:
     * 1. Validates the MFA challenge token
     * 2. Verifies the provided MFA code (TOTP)
     * 3. Issues the final JWT access token + refresh token
     *
     * SECURITY CONSIDERATIONS:
     * - MFA challenge token must be valid and non-expired
     * - Rate limiting should be applied (prevent brute force on MFA codes)
     * - Failed MFA attempts should log security events
     * - Token should include rotation mechanism if used for subsequent requests
     *
     * @param mfaRequest MFA challenge token and MFA code
     * @return MFAVerificationResponseDTO containing final accessToken + refreshToken
     * @throws InvalidMfaChallengeException if challenge token is invalid or expired
     * @throws MfaNotConfiguredException if user has no active MFA credentials
     * @throws InvalidCredentialsException if MFA code verification fails
     *
     * @see MFAVerificationRequestDTO for request structure
     * @see MFAVerificationResponseDTO for response structure
     */
    MFAVerificationResponseDTO verifyMfa(MFAVerificationRequestDTO mfaRequest);

    /**
     * Step 3: Logout user by revoking their refresh token.
     *
     * This method invalidates the user's refresh token, preventing
     * future token refresh operations.
     *
     * SECURITY CONSIDERATIONS:
     * - Refresh token is revoked in database
     * - User must authenticate again to get new tokens
     * - Should be called for password changes and security events
     *
     * @param refreshToken The refresh token to revoke
     */
    void logout(String refreshToken);

    /**
     * Step 4: Refresh access token using a refresh token.
     *
     * This method allows users to get a new access token without
     * re-authenticating with their password.
     *
     * SECURITY CONSIDERATIONS:
     * - Refresh token must be valid and not revoked
     * - Both old and new tokens should be tracked
     * - Implement rate limiting to prevent token refresh abuse
     *
     * @param refreshToken The refresh token JWT
     * @return AuthenticationResponseDTO with new accessToken and refreshToken
     * @throws InvalidCredentialsException if refresh token is invalid or expired
     */
    AuthenticationResponseDTO refreshAccessToken(String refreshToken);
}
