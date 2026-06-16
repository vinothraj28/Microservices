package com.microservices.profile.dto.address;

import com.microservices.profile.models.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddressRequestDTO(

        @NotNull
        AddressType addressType,

        @NotBlank
        String zipcode,

        @NotBlank
        String street,

        @NotBlank
        String province,

        @NotBlank
        String country,

        @NotNull
        UUID userId
) {
}
