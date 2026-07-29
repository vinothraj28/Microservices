package com.microservices.gateway.DTOS.movie;

public record MovieImage(
    long id,
    String fileName,
    String contentType,
    Long size,
    byte[] data, // temporary if storing in DB
    String storageKey, // used after moving to cloud storage
    String provider
) {
}
