package com.microservices.movie.exceptions;

public class ImageException extends RuntimeException {
    public ImageException(String message) {
        super(message);
    }
}
