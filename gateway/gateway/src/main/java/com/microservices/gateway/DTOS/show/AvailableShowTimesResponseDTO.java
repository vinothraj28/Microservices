package com.microservices.gateway.DTOS.show;

import java.util.List;

/**
 * DTO for available show slots response.
 *
 * @param theaterId Theater UUID
 * @param screenId Screen UUID
 * @param requestedDate Requested date (YYYY-MM-DD)
 * @param slots List of available slots for the date
 * @param totalCount Number of available slots
 */
public record AvailableShowTimesResponseDTO(
        String theaterId,
        String screenId,
        String requestedDate,
        List<ShowTimeSlotDTO> slots,
        Integer totalCount
) {
}
