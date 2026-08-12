package com.microservices.gateway.DTOS.screen;

public record SeatLayoutRequestDTO(
        String rowName,
        int startSeatNumber,
        int endSeatNumber,
        String seatType,
        double priceMultiplier
) {
}
