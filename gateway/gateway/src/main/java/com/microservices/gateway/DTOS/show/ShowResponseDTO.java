package com.microservices.gateway.DTOS.show;

import com.microservices.gateway.DTOS.movie.MovieResponseDTO;
import com.microservices.gateway.DTOS.screen.ScreenResponseDTO;

import java.time.LocalDateTime;

/**
 * DTO for show response
 * Contains complete show information including nested movie and screen details
 * 
 * @param showId Unique identifier for the show
 * @param movieId ID of the movie being shown
 * @param screenId ID of the screen
 * @param theaterId ID of the theater
 * @param movie Complete movie details
 * @param screen Complete screen details
 * @param showDateTime Date and time of the show
 * @param basePrice Base ticket price
 * @param showType Type of show (MORNING, MATINEE, EVENING, NIGHT)
 * @param availableSeats Number of available seats
 * @param createdAt Timestamp when show was created
 * @param updatedAt Timestamp when show was last updated
 */
public record ShowResponseDTO(
        String showId,
        String movieId,
        String screenId,
        String theaterId,
        MovieResponseDTO movie,
        ScreenResponseDTO screen,
        String showDateTime,
        Double basePrice,
        String showType,
        Integer availableSeats,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
