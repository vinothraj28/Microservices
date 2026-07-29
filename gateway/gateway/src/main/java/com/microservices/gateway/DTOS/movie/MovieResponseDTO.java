package com.microservices.gateway.DTOS.movie;

import java.time.LocalDateTime;
import java.util.List;

public record MovieResponseDTO(
        String movie_id,
        String title,
        String description,
        int duration_minutes,
        String genre,
        String language,
        String release_date,
        String poster_url,
        String trailer_url,
        String rating,
        List<String>cast,
        List<String> crew,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        String imageId
) {
}
