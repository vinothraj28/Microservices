package com.microservices.gateway.DTOS.errors;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponseDTO(
        int status,
        String message,
        String path,
        Map<String, String> errors,
        Instant timestamp,
        String traceId
) {
}
