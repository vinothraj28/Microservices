package com.microservices.gateway.DTOS.show;

/**
 * DTO for individual seat information in a show
 * 
 * @param seatId Unique identifier for the seat
 * @param rowName Row name (e.g., "A", "B", "C")
 * @param seatNumber Seat number within the row
 * @param seatType Type of seat (REGULAR, PREMIUM, RECLINER, VIP)
 * @param price Calculated price for this seat (basePrice * priceMultiplier)
 * @param status Seat status (AVAILABLE, LOCKED, BOOKED)
 * @param lockedUntil ISO datetime when lock expires (null if not locked)
 */
public record SeatInfoDTO(
        String seatId,
        String rowName,
        Integer seatNumber,
        String seatType,
        Double price,
        String status,
        String lockedUntil
) {
}
