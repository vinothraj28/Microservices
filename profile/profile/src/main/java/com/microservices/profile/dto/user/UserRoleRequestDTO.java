package com.microservices.profile.dto.user;

import com.microservices.profile.models.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserRoleRequestDTO(
        @NotNull
        Set<Roles> role
) {
}
