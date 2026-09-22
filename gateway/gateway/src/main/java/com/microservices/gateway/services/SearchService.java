package com.microservices.gateway.services;

import com.microservices.gateway.DTOS.movie.MovieResponseDTO;
import com.microservices.gateway.DTOS.search.SearchSuggestionDTO;
import com.microservices.gateway.DTOS.show.ShowListResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterResponseDTO;
import com.microservices.gateway.services.gRPCServices.MovieGrpcService;
import com.microservices.gateway.services.gRPCServices.ShowGRPCService;
import com.microservices.gateway.services.gRPCServices.TheaterGRPCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private static final int MIN_QUERY_LENGTH = 2;
    private static final int SOURCE_LIMIT = 5;
    private static final int MAX_TOTAL_SUGGESTIONS = 10;

    private final MovieGrpcService movieGrpcService;
    private final TheaterGRPCService theaterGRPCService;
    private final ShowGRPCService showGRPCService;

    public List<SearchSuggestionDTO> searchSuggestions(String q) {
        String query = normalizeQuery(q);
        if (query.length() < MIN_QUERY_LENGTH) {
            return List.of();
        }

        List<SearchSuggestionDTO> suggestions = new ArrayList<>();
        RuntimeException firstError = null;

        try {
            suggestions.addAll(mapMovieSuggestions(movieGrpcService.searchMovies(query, SOURCE_LIMIT)));
        } catch (RuntimeException e) {
            log.warn("Search suggestions: movie lookup failed for query '{}'", query, e);
            firstError = e;
        }

        try {
            suggestions.addAll(mapTheaterSuggestions(theaterGRPCService.searchTheaters(query, SOURCE_LIMIT)));
        } catch (RuntimeException e) {
            log.warn("Search suggestions: theater lookup failed for query '{}'", query, e);
            if (firstError == null) {
                firstError = e;
            }
        }

        if (suggestions.isEmpty() && firstError != null) {
            throw firstError;
        }

        return suggestions.stream()
                .limit(MAX_TOTAL_SUGGESTIONS)
                .toList();
    }

    public ShowListResponseDTO searchShows(
            String movieId,
            String theaterId,
            String date,
            String city,
            String showType,
            String genre,
            String language,
            int page,
            int size
    ) {
        String normalizedMovieId = normalizeOptional(movieId);
        String normalizedTheaterId = normalizeOptional(theaterId);
        String normalizedDate = normalizeOptional(date);
        String normalizedCity = normalizeOptional(city);
        String normalizedShowType = normalizeOptional(showType);
        String normalizedGenre = normalizeOptional(genre);
        String normalizedLanguage = normalizeOptional(language);

        int normalizedPage = Math.max(page, 0);
        int normalizedSize = size > 0 ? size : 20;

        if (normalizedShowType != null || normalizedGenre != null || normalizedLanguage != null) {
            return showGRPCService.searchShows(
                    normalizedMovieId,
                    normalizedTheaterId,
                    normalizedDate,
                    normalizedCity,
                    normalizedShowType,
                    normalizedGenre,
                    normalizedLanguage,
                    normalizedPage,
                    normalizedSize
            );
        }

        if (normalizedMovieId != null && normalizedTheaterId != null) {
            return showGRPCService.searchShows(
                    normalizedMovieId,
                    normalizedTheaterId,
                    normalizedDate,
                    normalizedCity,
                    null,
                    null,
                    null,
                    normalizedPage,
                    normalizedSize
            );
        }
        if (normalizedMovieId != null) {
            return showGRPCService.listShowsByMovie(normalizedMovieId, normalizedDate, normalizedCity);
        }
        if (normalizedTheaterId != null) {
            return showGRPCService.listShowsByTheater(normalizedTheaterId, normalizedDate);
        }
        if (normalizedDate!= null){
            return showGRPCService.searchShows(null, null, normalizedDate, null,
                    null, null,null,normalizedPage, normalizedSize);
        }
        return showGRPCService.getShows(normalizedPage, normalizedSize);
    }

    private List<SearchSuggestionDTO> mapMovieSuggestions(com.microservices.gateway.DTOS.movie.MovieListResponseDTO response) {
        if (response == null || response.movieResponseDTO() == null) {
            return List.of();
        }

        return response.movieResponseDTO().stream()
                .map(this::toMovieSuggestion)
                .toList();
    }

    private List<SearchSuggestionDTO> mapTheaterSuggestions(com.microservices.gateway.DTOS.theater.TheaterListResponseDTO response) {
        if (response == null || response.theaters() == null) {
            return List.of();
        }

        return response.theaters().stream()
                .map(this::toTheaterSuggestion)
                .toList();
    }

    private SearchSuggestionDTO toMovieSuggestion(MovieResponseDTO movie) {
        return new SearchSuggestionDTO(
                movie.movieId(),
                "MOVIE",
                movie.title(),
                buildMovieSubtitle(movie)
        );
    }

    private SearchSuggestionDTO toTheaterSuggestion(TheaterResponseDTO theater) {
        return new SearchSuggestionDTO(
                theater.theaterId(),
                "THEATER",
                theater.name(),
                theater.city()
        );
    }

    private String buildMovieSubtitle(MovieResponseDTO movie) {
        String genre = movie.genre() == null ? "" : movie.genre();
        String language = movie.language() == null ? "" : movie.language();
        if (genre.isBlank()) {
            return language;
        }
        if (language.isBlank()) {
            return genre;
        }
        return genre + " · " + language;
    }

    private String normalizeQuery(String q) {
        return q == null ? "" : q.trim();
    }

    private String normalizeOptional(String value) {
        String normalized = normalizeQuery(value);
        return normalized.isEmpty() ? null : normalized;
    }
}
