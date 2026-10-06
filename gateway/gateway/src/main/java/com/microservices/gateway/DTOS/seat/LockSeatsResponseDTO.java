package com.microservices.gateway.DTOS.seat;

import java.util.List;

/**
 * DTO for locking seats response
 * Contains lock ID and expiry information
 * 
 * @param success Whether lock operation was successful
 * @param lockId Unique lock identifier (use in CreateBookingRequest)
 * @param lockedUntil ISO datetime when lock expires (5 minutes)
 * @param lockedSeatIds List of successfully locked seat IDs
 * @param message Operation status message
 */
public record LockSeatsResponseDTO(
        Boolean success,
        String lockId,
        String lockedUntil,
        List<String> lockedSeatIds,
        String message
) {
}
