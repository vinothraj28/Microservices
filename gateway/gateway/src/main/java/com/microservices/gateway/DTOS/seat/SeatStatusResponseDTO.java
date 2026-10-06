package com.microservices.gateway.DTOS.seat;

import java.util.List;

/**
 * DTO for seat status response
 * 
 * @param seatStatuses List of seat status information
 */
public record SeatStatusResponseDTO(
        List<SeatStatusDTO> seatStatuses
) {
}
