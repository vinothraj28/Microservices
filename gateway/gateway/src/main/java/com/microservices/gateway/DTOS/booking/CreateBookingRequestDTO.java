package com.microservices.gateway.DTOS.booking;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO for creating a booking request
 * Contains user, show, and seat information
 * 
 * @param userId UUID of the user making the booking
 * @param showId UUID of the show being booked
 * @param seatIds List of seat IDs to book
 * @param email Email for booking confirmation
 * @param phone Phone number for booking contact
 */
public record CreateBookingRequestDTO(
        @NotBlank(message = "User ID is required")
        String userId,

        @NotBlank(message = "Show ID is required")
        String showId,

        @NotNull(message = "Seat IDs are required")
        @NotEmpty(message = "At least one seat ID is required")
        List<String> seatIds,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        String phone
) {
}
