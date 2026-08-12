package com.microservices.gateway.DTOS.seat;

public record Seat(
        String row_name,
        int start_seat_number,
        int end_seat_number,
        String seat_type,
        double price_multiplier
) {
}
