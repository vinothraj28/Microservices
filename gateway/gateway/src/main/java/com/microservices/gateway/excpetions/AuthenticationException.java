package com.microservices.gateway.excpetions;

/**
 * Exception thrown when authentication fails.
 *
 * This includes:
 * - Invalid credentials
 * - Invalid MFA code
 * - Expired MFA challenge token
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

