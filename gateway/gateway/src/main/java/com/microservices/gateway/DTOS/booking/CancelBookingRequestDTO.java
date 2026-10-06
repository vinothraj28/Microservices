package com.microservices.gateway.DTOS.booking;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for cancelling a booking
 * 
 * @param bookingId UUID of the booking to cancel
 * @param reason Reason for cancellation
 */
public record CancelBookingRequestDTO(
        @NotBlank(message = "Booking ID is required")
        String bookingId,

        String reason
) {
}
