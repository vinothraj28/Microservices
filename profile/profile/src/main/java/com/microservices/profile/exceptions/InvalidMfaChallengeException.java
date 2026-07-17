package com.microservices.profile.exceptions;

/**
 * Exception thrown when MFA challenge token is invalid, expired, or cannot be verified.
 *
 * This indicates that the user is attempting MFA verification without a valid
 * authentication challenge token, or the token has expired.
 *
 * Scenario: User tries to verify MFA with an expired or tampered challenge token.
 */
public class InvalidMfaChallengeException extends RuntimeException {

    public InvalidMfaChallengeException(String message) {
        super(message);
    }

    public InvalidMfaChallengeException(String message, Throwable cause) {
        super(message, cause);
    }
}

