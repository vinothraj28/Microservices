package com.microservices.gateway.DTOS.show;

import jakarta.validation.constraints.*;

/**
 * DTO for creating a show
 * Validation constraints ensure data integrity at API layer
 * 
 * @param movieId UUID of the movie
 * @param screenId UUID of the screen
 * @param showDateTime ISO-8601 format datetime string (e.g., "2024-12-25T18:30:00")
 * @param basePrice Base ticket price (must be positive)
 * @param showType Type of show: MORNING, MATINEE, EVENING, NIGHT
 */
public record CreateShowRequestDTO(
        @NotBlank(message = "Movie ID is required")
        String movieId,

        @NotBlank(message = "Screen ID is required")
        String screenId,

        @NotBlank(message = "Theater ID is required")
        String theaterId,

        @NotBlank(message = "Show date time is required")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", 
                 message = "Show date time must be in ISO-8601 format (yyyy-MM-ddTHH:mm:ss)")
        String showDateTime,

        @NotNull(message = "Base price is required")
        @Positive(message = "Base price must be greater than zero")
        @DecimalMin(value = "0.01", message = "Base price must be at least 0.01")
        Double basePrice,

        @NotBlank(message = "Show type is required")
        @Pattern(regexp = "MORNING|MATINEE|EVENING|NIGHT", 
                 message = "Show type must be one of: MORNING, MATINEE, EVENING, NIGHT")
        String showType
) {
}
