package com.microservices.movie.mappers;

import com.google.protobuf.Timestamp;
import com.microservices.movie.grpc.*;
import com.microservices.movie.models.entities.Movie;
import com.microservices.movie.models.entities.Screen;
import com.microservices.movie.models.entities.Show;
import com.microservices.movie.models.enums.ShowType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.UUID;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * MapStruct mapper for Show entity and gRPC proto conversions
 * Time Complexity: O(1) for single mappings, O(n) for collections
 * Space Complexity: O(1) for single mappings
 * 
 * Follows SOLID principles:
 * - Single Responsibility: Handles only Show mapping
 * - Open/Closed: Extensible via MapStruct custom methods
 * - Interface Segregation: Focused interface for Show operations
 */
@Mapper(componentModel = "spring", uses = {MovieMapper.class, ScreenMapper.class, DateTimeMapper.class})
public interface ShowMapper {

    /**
     * Maps CreateShowRequest to Show entity
     * O(1) time complexity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", expression = "java(buildMovie(request.getMovieId()))")
    @Mapping(target = "screen", expression = "java(buildScreen(request.getScreenId()))")
    @Mapping(target = "theaterId", expression = "java(parseTheaterId(request.getTheaterId()))")
    @Mapping(target = "showDateTime", expression = "java(parseDateTime(request.getShowDateTime()))")
    @Mapping(target = "basePrice", source = "basePrice")
    @Mapping(target = "showType", expression = "java(parseShowType(request.getShowType()))")
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Show toShow(CreateShowRequest request);

    /**
     * Maps UpdateShowRequest to Show entity
     * O(1) time complexity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "screen", ignore = true)
    @Mapping(target = "theaterId", ignore = true)
    @Mapping(target = "showDateTime", expression = "java(parseDateTime(request.getShowDateTime()))")
    @Mapping(target = "basePrice", source = "basePrice")
    @Mapping(target = "showType", expression = "java(parseShowType(request.getShowType()))")
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Show toShow(UpdateShowRequest request);

    /**
     * Maps Show entity to ShowResponse proto
     * Includes nested movie and screen details
     * O(1) time complexity
     */
    @Mapping(target = "showId", expression = "java(show.getId().toString())")
    @Mapping(target = "movieId", expression = "java(show.getMovie().getId().toString())")
    @Mapping(target = "screenId", expression = "java(show.getScreen().getId().toString())")
    @Mapping(target = "theaterId", expression = "java(show.getTheaterId().toString())")
    @Mapping(target = "movie", expression = "java(mapMovieToProto(show.getMovie(), movieMapper))")
    @Mapping(target = "screen", expression = "java(mapScreenToProto(show.getScreen(), screenMapper))")
    @Mapping(target = "showDateTime", expression = "java(formatDateTime(show.getShowDateTime()))")
    @Mapping(target = "basePrice", expression = "java(show.getBasePrice())")
    @Mapping(target = "showType", expression = "java(show.getShowType().name())")
    @Mapping(target = "availableSeats", expression = "java(show.getAvailableSeatsCount())")
    @Mapping(target = "theaterName", expression = "java(show.getScreen().getTheater().getTheaterName())")
    ShowResponse toShowResponse(Show show, MovieMapper movieMapper, ScreenMapper screenMapper);

    // Helper methods for complex mappings

    default Movie buildMovie(String movieId) {
        if (movieId == null || movieId.isBlank()) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(java.util.UUID.fromString(movieId));
        return movie;
    }

    default Screen buildScreen(String screenId) {
        if (screenId == null || screenId.isBlank()) {
            return null;
        }
        Screen screen = new Screen();
        screen.setId(java.util.UUID.fromString(screenId));
        return screen;
    }

    default UUID parseTheaterId(String theaterId) {
        if (theaterId == null || theaterId.isBlank()) {
            return null;
        }
        return java.util.UUID.fromString(theaterId);
    }

    default LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    default String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    default ShowType parseShowType(String showTypeStr) {
        if (showTypeStr == null || showTypeStr.isBlank()) {
            return null;
        }
        return ShowType.valueOf(showTypeStr.toUpperCase());
    }

    default MovieResponse mapMovieToProto(Movie movie, MovieMapper movieMapper) {
        if (movie == null) {
            return MovieResponse.getDefaultInstance();
        }
        return MovieResponse.newBuilder()
                .setMovieId(movie.getId().toString())
                .setTitle(movie.getTitle())
                .setDescription(movie.getDescription() != null ? movie.getDescription() : "")
                .setDurationMinutes(movie.getDurationMinutes())
                .setGenre(movie.getGenre())
                .setLanguage(movie.getLanguage())
                .setReleaseDate(movie.getReleaseDate().toString())
                .setPosterUrl(movie.getPosterUrl() != null ? movie.getPosterUrl() : "")
                .setTrailerUrl(movie.getTrailerUrl() != null ? movie.getTrailerUrl() : "")
                .setRating(movie.getRating())
                .addAllCast(movie.getCast())
                .addAllCrew(movie.getCrew())
                .setImageId(movie.getImage() != null ? String.valueOf(movie.getImage().getId()) : "")
                .build();
    }

    default ScreenResponse mapScreenToProto(Screen screen, ScreenMapper screenMapper) {
        if (screen == null) {
            return ScreenResponse.getDefaultInstance();
        }
        return screenMapper.toScreenResponse(screen);
    }


}
