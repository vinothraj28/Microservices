package com.microservices.gateway.DTOS.show;

import java.util.List;

/**
 * DTO for available seats response
 * Contains all seats for a show with their availability status
 * 
 * @param showId ID of the show
 * @param seats List of all seats with availability information
 * @param totalAvailable Count of seats with AVAILABLE status
 */
public record AvailableSeatsResponseDTO(
        String showId,
        List<SeatInfoDTO> seats,
        Integer totalAvailable
) {
}
