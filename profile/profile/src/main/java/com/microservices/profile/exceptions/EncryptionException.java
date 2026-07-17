package com.microservices.profile.exceptions;

public class EncryptionException extends RuntimeException {
    public EncryptionException(String message) {
        super(message);
    }
}
