package com.microservices.profile.exceptions;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
}
