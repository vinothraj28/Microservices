package com.microservices.gateway.DTOS.seat;

/**
 * DTO for individual seat status
 * 
 * @param seatId Unique seat identifier
 * @param status Seat status (AVAILABLE, LOCKED, BOOKED)
 * @param lockedBy User ID who locked the seat (if locked)
 * @param lockedUntil ISO datetime when lock expires (if locked)
 */
public record SeatStatusDTO(
        String seatId,
        String status,
        String lockedBy,
        String lockedUntil
) {
}
