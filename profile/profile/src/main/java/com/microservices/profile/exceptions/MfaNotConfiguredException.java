package com.microservices.profile.exceptions;

/**
 * Exception thrown when a user attempts MFA verification but has not set up MFA credentials.
 *
 * This indicates an inconsistent state where:
 * - The authentication system issued an MFA challenge token
 * - But the user has no active MFA credentials configured
 *
 * Scenario: User setup flow is incomplete or credentials were deleted between auth and MFA verify.
 */
public class MfaNotConfiguredException extends RuntimeException {

    public MfaNotConfiguredException(String message) {
        super(message);
    }

    public MfaNotConfiguredException(String message, Throwable cause) {
        super(message, cause);
    }
}

