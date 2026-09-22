package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Movie;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface MovieService {

    enum DeleteResult {
        SUCCESS, NOT_FOUND, HAS_ACTIVE_SHOWS, CONFLICT
    }

    Movie createMovie(Movie movie);

    Movie updateMovie(UUID movieId, Movie movie);

    Movie getMovie(UUID movieId);

    Page<Movie> listMovies(int page, int size, String genre, String language);

    Page<Movie> searchMovies(String query, int limit);

    DeleteResult deleteMovie(UUID movieId);
}
