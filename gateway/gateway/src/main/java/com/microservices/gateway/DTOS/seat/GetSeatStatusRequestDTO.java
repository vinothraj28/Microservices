package com.microservices.gateway.DTOS.seat;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO for getting seat status request
 * Check availability of specific seats
 * 
 * @param showId UUID of the show
 * @param seatIds List of seat IDs to check
 */
public record GetSeatStatusRequestDTO(
        @NotBlank(message = "Show ID is required")
        String showId,

        @NotNull(message = "Seat IDs are required")
        @NotEmpty(message = "At least one seat ID is required")
        List<String> seatIds
) {
}
