package com.microservices.gateway.DTOS.theater;

import java.util.List;

public record TheaterRequestDTO(
        String name,
        String address,
        String city,
        String state,
        String pincode,
        double latitude,
        double longitude,
        String phone,
        String email,
        List<String> amenities
        ) {
}
