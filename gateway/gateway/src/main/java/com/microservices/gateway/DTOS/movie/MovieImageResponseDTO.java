package com.microservices.gateway.DTOS.movie;

public record MovieImageResponseDTO(
        byte[] data,
        String fileName,
        String contentType,
        long size
) {
}
