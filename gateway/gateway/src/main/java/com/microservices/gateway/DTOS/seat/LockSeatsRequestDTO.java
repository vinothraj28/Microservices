package com.microservices.gateway.DTOS.seat;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO for locking seats request
 * Locks seats for a specific show for 5 minutes
 * 
 * @param showId UUID of the show
 * @param seatIds List of seat IDs to lock
 * @param userId UUID of the user locking seats
 */
public record LockSeatsRequestDTO(
        @NotBlank(message = "Show ID is required")
        String showId,

        @NotNull(message = "Seat IDs are required")
        @NotEmpty(message = "At least one seat ID is required")
        List<String> seatIds,

        @NotBlank(message = "User ID is required")
        String userId
) {
}
