package com.microservices.gateway.DTOS.booking;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for confirming a booking with payment
 * 
 * @param bookingId UUID of the booking to confirm
 * @param paymentId UUID of the completed payment
 */
public record ConfirmBookingRequestDTO(
        @NotBlank(message = "Booking ID is required")
        String bookingId,

        @NotBlank(message = "Payment ID is required")
        String paymentId
) {
}
