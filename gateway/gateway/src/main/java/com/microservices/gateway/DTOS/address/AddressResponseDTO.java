package com.microservices.gateway.DTOS.address;

import java.util.UUID;

public record AddressResponseDTO(
        Long addressId,
        String zipcode,
        String street,
        String province,
        String country,
        UUID userId
) {
}
