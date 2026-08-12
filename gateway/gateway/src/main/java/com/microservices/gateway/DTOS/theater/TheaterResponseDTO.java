package com.microservices.gateway.DTOS.theater;

import com.microservices.movie.grpc.ScreenResponse;

import java.time.LocalDate;
import java.util.List;

public record TheaterResponseDTO(
    String theaterId,
    String name,
    String address,
    String city,
    String state,
    String pincode,
    double latitude,
    double longitude,
    String phone,
    String email,
    List<String> amenities,
    //List<ScreenResponse> screens,   //Retrieve screen separately
    LocalDate created_at,
    LocalDate updated_at
) {
}
