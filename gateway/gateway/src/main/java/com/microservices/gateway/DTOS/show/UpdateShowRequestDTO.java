package com.microservices.gateway.DTOS.show;

import jakarta.validation.constraints.*;

/**
 * DTO for updating a show
 * All fields except showId are optional for partial updates
 * 
 * @param showId UUID of the show to update (required)
 * @param showDateTime New show date time (optional)
 * @param basePrice New base price (optional)
 * @param showType New show type (optional)
 */
public record UpdateShowRequestDTO(
        @NotBlank(message = "Show ID is required")
        String showId,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", 
                 message = "Show date time must be in ISO-8601 format (yyyy-MM-ddTHH:mm:ss)")
        String showDateTime,

        @Positive(message = "Base price must be greater than zero")
        @DecimalMin(value = "0.01", message = "Base price must be at least 0.01")
        Double basePrice,

        @Pattern(regexp = "MORNING|MATINEE|EVENING|NIGHT", 
                 message = "Show type must be one of: MORNING, MATINEE, EVENING, NIGHT")
        String showType
) {
}
