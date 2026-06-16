package com.microservices.profile.dto.error;

import java.time.Instant;

public record ErrorResponseDTO(
        int status,
        String message,
        String path,
        Instant timestamp,
        String traceId
) {
}
