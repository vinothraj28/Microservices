package com.microservices.gateway.DTOS.register;

import java.time.LocalDate;

import java.util.Set;
import java.util.UUID;

public record RegisterResponseDTO(
        UUID userId,
        String userName,
        String emailAddress,
        LocalDate dob,
        Set<String> roles
) {
}
