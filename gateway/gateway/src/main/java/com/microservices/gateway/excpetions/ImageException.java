package com.microservices.gateway.excpetions;

public class ImageException extends RuntimeException {
    public ImageException(String message) {
        super(message);
    }
}
