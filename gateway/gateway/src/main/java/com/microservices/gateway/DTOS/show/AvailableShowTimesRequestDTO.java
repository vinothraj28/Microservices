package com.microservices.gateway.DTOS.show;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * DTO for requesting available show slots for a screen on a date.
 *
 * @param theaterId Theater UUID
 * @param screenId Screen UUID
 * @param movieRunTime Runtime in minutes
 * @param requestedShowDateTime Requested datetime in ISO-8601 format
 */
public record AvailableShowTimesRequestDTO(
        @NotBlank(message = "Theater ID is required")
        String theaterId,

        @NotBlank(message = "Screen ID is required")
        String screenId,

        @NotNull(message = "Movie runtime is required")
        @Positive(message = "Movie runtime must be greater than zero")
        Integer movieRunTime,

        @NotBlank(message = "Requested show date time is required")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}",
                message = "Requested show date time must be in ISO-8601 format (yyyy-MM-ddTHH:mm:ss)")
        String requestedShowDateTime
) {
}
