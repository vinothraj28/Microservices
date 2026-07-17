package com.microservices.profile.services.Impl;

import com.microservices.profile.dto.auth.AuthenticationRequestDTO;
import com.microservices.profile.dto.auth.AuthenticationResponseDTO;
import com.microservices.profile.dto.auth.MFAVerificationRequestDTO;
import com.microservices.profile.dto.auth.MFAVerificationResponseDTO;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.InvalidMfaChallengeException;
import com.microservices.profile.exceptions.MfaNotConfiguredException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.services.AuthenticationService;
import com.microservices.profile.services.RefreshTokenService;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.spi.managers.CredentialsManager;
import com.microservices.profile.spi.managers.UserManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of AuthenticationService.
 *
 * Orchestrates the complete authentication flow:
 * 1. AUTHENTICATE: Validate credentials and determine MFA requirement
 * 2. VERIFY_MFA: Verify MFA code and issue final token
 * 3. LOGOUT: Revoke refresh tokens
 * 4. REFRESH_TOKEN: Issue new tokens from refresh token
 *
 * ARCHITECTURE:
 * This is an Application Service that coordinates domain operations without
 * containing business logic itself. It ensures:
 * - Single entry point for authentication flows
 * - Clear separation of concerns (credential validation, token generation, MFA verification)
 * - Transactional consistency
 * - Comprehensive error handling and logging
 *
 * DEPENDENCIES:
 * - UserManager: User lookup and persistence
 * - CredentialsManager: Credential validation and MFA status checking
 * - TokenService: JWT generation and parsing (includes MFA challenge tokens)
 * - RefreshTokenService: Refresh token management and revocation
 *
 * CONCURRENCY:
 * - Leverages database-level constraints for credential uniqueness
 * - Uses @Transactional for consistency
 * - No explicit locking needed (optimistic approach via entity versions)
 *
 * @see com.microservices.profile.services.AuthenticationService for interface contract
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserManager userManager;
    private final CredentialsManager credentialsManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    // MFA challenge token lifespan: 5 minutes
    private static final long MFA_CHALLENGE_EXPIRY_SECONDS = 300;

    public AuthenticationServiceImpl(
            UserManager userManager,
            CredentialsManager credentialsManager,
            TokenService tokenService,
            RefreshTokenService refreshTokenService
    ) {
        this.userManager = userManager;
        this.credentialsManager = credentialsManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Step 1: Authenticate user and determine MFA requirement.
     *
     * FLOW:
     * 1. Lookup user by email
     * 2. Validate password credential
     * 3. Check if MFA (TOTP) is enabled
     * 4. Return appropriate response:
     *    - If MFA disabled: return JWT access token + refresh token
     *    - If MFA enabled: return MFA challenge token
     *
     * ERROR HANDLING:
     * - Generic error message for invalid credentials (prevents user enumeration)
     * - Logs detailed info for monitoring
     * - Transactional read prevents dirty reads
     *
     * IDEMPOTENCY:
     * Multiple calls with same credentials will return same response
     * (no side effects during authentication)
     *
     * @param authRequest Email and password
     * @return AuthenticationResponseDTO with access or challenge token
     */
    @Override
    @Transactional
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authRequest) {
        log.info("Authentication attempt for email: {}", authRequest.email());

        // Step 1: Lookup user
        Optional<UserProfile> userOptional = userManager.getUserByEmailAddress(authRequest.email());
        if (userOptional.isEmpty()) {
            log.warn("Authentication failed: user not found for email: {}", authRequest.email());
            throw new InvalidCredentialsException("Email or password is incorrect");
        }

        UserProfile user = userOptional.get();

        // Step 2: Validate password
        boolean passwordValid = credentialsManager.validateCredential(
                user,
                CredentialsType.PASSWORD,
                authRequest.password()
        );

        if (!passwordValid) {
            log.warn("Authentication failed: invalid password for email: {}", authRequest.email());
            throw new InvalidCredentialsException("Email or password is incorrect");
        }

        log.debug("Password validation successful for user: {}", authRequest.email());

        // Step 3: Check if MFA is enabled
        Optional<Credential> mfaCredential = credentialsManager.getActiveCredential(
                user,
                CredentialsType.TOTP
        );

        if (mfaCredential.isEmpty()) {
            // MFA disabled: Issue JWT token + refresh token
            log.info("Authentication successful (MFA not required) for email: {}", authRequest.email());
            String accessToken = tokenService.generateToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);
            
            // Save refresh token to database
            refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);
            
            return AuthenticationResponseDTO.successWithoutMfa(accessToken, refreshToken);
        }

        // MFA enabled: Issue challenge token
        log.info("Authentication successful but MFA required for email: {}", authRequest.email());
        String mfaChallengeToken = generateMfaChallengeToken(user);
        return AuthenticationResponseDTO.mfaChallengeRequired(mfaChallengeToken);
    }

    /**
     * Step 2: Verify MFA code and issue final JWT token.
     *
     *
     * FLOW:
     * 1. Parse and validate MFA challenge token
     * 2. Extract user context from challenge token
     * 3. Verify MFA code against user's TOTP credential
     * 4. Generate and return final JWT access token + refresh token
     *
     * SECURITY:
     * - Challenge token must be valid and non-expired
     * - Challenge token includes user ID to prevent token swapping
     * - MFA code verification has rate limiting (external concern)
     *
     * ERROR HANDLING:
     * - InvalidMfaChallengeException for token issues
     * - MfaNotConfiguredException if credential missing (state error)
     * - InvalidCredentialsException if code verification fails
     *
     * IDEMPOTENCY:
     * Multiple calls with same valid code within 30-second window may succeed
     * (depends on TOTP implementation - typically time-based codes)
     *
     * @param mfaRequest Challenge token and MFA code
     * @return MFAVerificationResponseDTO with JWT token
     */
    @Override
    @Transactional(readOnly = true)
    public MFAVerificationResponseDTO verifyMfa(MFAVerificationRequestDTO mfaRequest) {
        log.debug("MFA verification attempt initiated");

        // Step 1: Parse and validate challenge token
        String userId;
        try {
            Claims claims = tokenService.extractClaims(mfaRequest.mfaChallengeToken());

            // Verify token type (ensure it's a challenge token, not a regular token)
            Object tokenType = claims.get("type");
            if (tokenType == null || !tokenType.equals("mfa_challenge")) {
                log.warn("MFA verification failed: invalid token type");
                throw new InvalidMfaChallengeException("Invalid MFA challenge token");
            }

            userId = claims.getSubject();
            log.debug("Successfully decoded MFA challenge token for userId: {}", userId);
        } catch (JwtException e) {
            log.warn("MFA verification failed: challenge token invalid or expired", e);
            throw new InvalidMfaChallengeException(
                    "MFA challenge token is invalid or expired. Please authenticate again.",
                    e
            );
        }

        // Step 2: Lookup user from challenge token
        java.util.UUID userUUID;
        try {
            userUUID = java.util.UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            log.warn("MFA verification failed: invalid userId in token: {}", userId);
            throw new InvalidMfaChallengeException("Invalid MFA challenge token");
        }

        Optional<UserProfile> userOptional = userManager.getUserById(userUUID);
        if (userOptional.isEmpty()) {
            log.warn("MFA verification failed: user not found for userId: {}", userId);
            throw new InvalidMfaChallengeException("User context not found. Please authenticate again.");
        }

        UserProfile user = userOptional.get();
        log.debug("User context resolved for MFA verification: {}", user.getEmailAddress());

        // Step 3: Verify MFA code
        boolean codeValid = credentialsManager.validateCredential(
                user,
                CredentialsType.TOTP,
                mfaRequest.mfaCode()
        );

        if (!codeValid) {
            log.warn("MFA verification failed: invalid MFA code for user: {}", user.getEmailAddress());
            throw new InvalidCredentialsException("Invalid MFA code");
        }

        log.debug("MFA code verification successful for user: {}", user.getEmailAddress());

        // Step 4: Generate final JWT token + refresh token
        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        
        // Save refresh token to database
        refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);
        
        log.info("MFA verification successful and JWT token generated for user: {}", user.getEmailAddress());

        return MFAVerificationResponseDTO.success(accessToken, refreshToken);
    }

    /**
     * Step 3: Logout user by revoking their refresh token.
     *
     * Marks the refresh token as revoked in the database so it cannot be
     * used to obtain new access tokens.
     *
     * @param refreshToken The refresh token to revoke
     */
    @Override
    @Transactional
    public void logout(String refreshToken) {
        log.info("Logout request received");
        try{
            refreshTokenService.revokeRefreshToken(refreshToken);
            log.info("User logged out successfully");
        }catch (Exception e){
            log.warn("Logout failed: {}", e.getMessage());
        }
    }

    /**
     * Step 4: Refresh access token using a refresh token.
     *
     * Validates the refresh token and issues new access and refresh tokens.
     *
     * @param refreshToken The refresh token JWT
     * @return AuthenticationResponseDTO with new accessToken and refreshToken
     */
    @Override
    @Transactional
    public AuthenticationResponseDTO refreshAccessToken(String refreshToken) {
        log.info("Refresh token request received");
        
        RefreshTokenService.RefreshTokenResult result = refreshTokenService.refreshAccessToken(refreshToken);
        
        log.info("Access token refreshed successfully");
        
        return AuthenticationResponseDTO.successWithoutMfa(result.accessToken(), result.refreshToken());
    }

    /**
     * Generates a short-lived MFA challenge token.
     *
     * The challenge token:
     * - Contains the user's ID (for state tracking)
     * - Marked with type="mfa_challenge" (to prevent token type confusion)
     * - Expires in 5 minutes (configurable)
     * - Allows secure state transfer between authentication and MFA verification steps
     *
     * This token does NOT grant access; it only authorizes MFA verification.
     *
     * SECURITY:
     * - Short lifespan prevents offline attacks
     * - Marked with type prevents confusion with access tokens
     * - Includes user context to prevent token swapping between users
     *
     * @param user User to create challenge token for
     * @return MFA challenge token string
     */
    private String generateMfaChallengeToken(UserProfile user) {
        log.debug("Generating MFA challenge token for user: {}", user.getEmailAddress());

        // Use TokenService to generate challenge token with special type marker
        // The implementation depends on how TokenService supports custom claims
        // Here we assume TokenService has a method to add custom claims
        return tokenService.generateMfaChallengeToken(user);
    }
}

