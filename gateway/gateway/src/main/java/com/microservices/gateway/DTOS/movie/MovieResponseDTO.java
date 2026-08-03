package com.microservices.gateway.DTOS.movie;

import java.time.LocalDateTime;
import java.util.List;

public record MovieResponseDTO(
        String movieId,
        String title,
        String description,
        int durationMinutes,
        String genre,
        String language,
        String releaseDate,
        String posterUrl,
        String trailerUrl,
        String rating,
        List<String>cast,
        List<String> crew,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String imageId
) {
}
