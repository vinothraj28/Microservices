package com.microservices.profile.dto.user;

import com.microservices.profile.models.enums.Roles;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


public record UserResponseDTO(
         UUID userId,
         String userName,
         String emailAddress,
         LocalDate dob,
         Set<String> roles
) {
}
