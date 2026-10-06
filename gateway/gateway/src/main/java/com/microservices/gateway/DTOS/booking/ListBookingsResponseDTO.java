package com.microservices.gateway.DTOS.booking;

import java.util.List;

/**
 * DTO for list of bookings response
 * Includes pagination metadata
 * 
 * @param bookings List of booking responses
 * @param totalCount Total number of bookings
 */
public record ListBookingsResponseDTO(
        List<BookingResponseDTO> bookings,
        Integer totalCount
) {
}
