package com.microservices.profile.exceptions;

public class OAuth2ValidationException extends RuntimeException {

    public OAuth2ValidationException(String message) {
        super(message);
    }

    public OAuth2ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
