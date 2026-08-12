package com.microservices.gateway.DTOS.screen;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddScreenRequestDTO(
        @NotBlank(message = "Theater ID is required")
        String theaterId,
        
        @NotBlank(message = "Screen name is required")
        String screenName,
        
        @Min(value = 1, message = "Screen number must be at least 1")
        int screenNumber,
        
        @Min(value = 1, message = "Total rows must be at least 1")
        int totalRows,

        @Min(value = 1, message = "Total seats must be at least 1")
        int totalSeats,

        @NotBlank(message = "Screen type is required")
        String screenType,
        
        @NotNull(message = "Seat layout is required")
        List<SeatLayoutRequestDTO> seatLayout
) {
}
