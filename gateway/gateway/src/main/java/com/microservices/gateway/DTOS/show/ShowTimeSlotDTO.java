package com.microservices.gateway.DTOS.show;

/**
 * DTO for an available show slot.
 *
 * @param startTime Slot start time in ISO-8601 format
 * @param endTime Slot end time in ISO-8601 format
 */
public record ShowTimeSlotDTO(
        String startTime,
        String endTime
) {
}
