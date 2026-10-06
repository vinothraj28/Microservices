package com.microservices.gateway.DTOS.ticket;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for generating tickets request
 * 
 * @param bookingId UUID of the booking for which to generate tickets
 */
public record GenerateTicketsRequestDTO(
        @NotBlank(message = "Booking ID is required")
        String bookingId
) {
}
