package com.microservices.movie.exceptions;

public class SeatLockException extends RuntimeException {
    public SeatLockException(String message) {
        super(message);
    }
}
