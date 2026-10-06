package com.microservices.gateway.DTOS.booking;

import com.microservices.gateway.DTOS.show.ShowResponseDTO;
import com.microservices.gateway.DTOS.show.SeatInfoDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for booking response
 * Contains complete booking information including show and seats
 * 
 * @param bookingId Unique identifier for the booking
 * @param userId ID of the user who made the booking
 * @param showId ID of the show
 * @param show Complete show details
 * @param seats List of booked seats
 * @param totalAmount Total amount for the booking
 * @param status Booking status (PENDING, CONFIRMED, CANCELLED, EXPIRED)
 * @param email Email for booking
 * @param phone Phone for booking
 * @param lockId ID of the seat lock
 * @param expiresAt When the booking lock expires
 * @param createdAt Timestamp when booking was created
 * @param updatedAt Timestamp when booking was last updated
 */
public record BookingResponseDTO(
        String bookingId,
        String userId,
        String showId,
        ShowResponseDTO show,
        List<SeatInfoDTO> seats,
        Double totalAmount,
        String status,
        String email,
        String phone,
        String lockId,
        String expiresAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
