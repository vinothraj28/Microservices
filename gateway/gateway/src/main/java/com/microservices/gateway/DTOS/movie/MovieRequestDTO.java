package com.microservices.gateway.DTOS.movie;

import java.util.List;

public record MovieRequestDTO(
        String title,
        String description,
        int durationMinutes,
        String genre,
        String language,
        String releaseDate, // ISO date format YYYY-MM-DD
        String posterUrl,
        String trailerUrl,
        String rating, // U, UA, A, R
        List<String> cast,
        List<String> crew
) {
}
