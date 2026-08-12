package com.microservices.gateway.DTOS.screen;

public record SeatLayoutResponseDTO(
        String rowName,
        int startSeatNumber,
        int endSeatNumber,
        String seatType,
        double priceMultiplier
) {
}
