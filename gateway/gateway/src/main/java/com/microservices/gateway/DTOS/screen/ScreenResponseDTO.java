package com.microservices.gateway.DTOS.screen;

import java.time.LocalDateTime;
import java.util.List;

public record ScreenResponseDTO(
        String screenId,
        String theaterId,
        String screenName,
        int screenNumber,
        int totalSeats,
        String screenType,
        List<SeatLayoutResponseDTO> seatLayout,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
