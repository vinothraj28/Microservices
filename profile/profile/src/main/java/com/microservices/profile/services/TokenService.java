package com.microservices.profile.services;

import com.microservices.profile.models.entities.UserProfile;
import io.jsonwebtoken.Claims;

public interface TokenService {

    String generateToken(UserProfile userProfile);

    String generateRefreshToken(UserProfile userProfile);

    Claims extractClaims(String token);

    String extractUserId(String token);

    String extractJti(String token);

    boolean isValid(String token);

    /**
     * Generates a short-lived MFA challenge token.
     *
     * This token is returned after successful credential validation but before MFA verification.
     * It contains the user's ID and is marked with type="mfa_challenge" to prevent misuse as an access token.
     *
     * The token typically expires in 5 minutes and is used only for the MFA verification step.
     *
     * @param userProfile User to create challenge token for
     * @return MFA challenge token (JWT with type and short expiry)
     */
    String generateMfaChallengeToken(UserProfile userProfile);
}
