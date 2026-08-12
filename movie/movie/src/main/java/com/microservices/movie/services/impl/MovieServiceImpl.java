package com.microservices.movie.services.impl;

import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.models.entities.Movie;
import com.microservices.movie.repositories.MovieRepository;
import com.microservices.movie.services.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public Movie createMovie(Movie movie) {
        log.info("Creating movie with title: {}", movie.getTitle());
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovie(UUID movieId, Movie movie) {
        log.info("Updating movie with id: {}", movieId);
        Movie existingMovie = getMovie(movieId);
        mergeMovie(existingMovie, movie);
        return movieRepository.save(existingMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getMovie(UUID movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", movieId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Movie> listMovies(int page, int size, String genre, String language) {
        log.debug("Listing all movies");
        //Pageable pageable = Pageable.ofSize(size).withPage(page);
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        if(genre.isEmpty() && language.isEmpty()) {
            return movieRepository.findAll(pageable);
        }
        return movieRepository.findByGenreAndLanguage(genre, language, pageable);
    }

@Override
@Transactional
public MovieService.DeleteResult deleteMovie(UUID movieId) {
    Movie movie = movieRepository.findById(movieId)
        .orElse(null);

    if (movie == null) return MovieService.DeleteResult.NOT_FOUND;

    // Business validation: Don't delete if active shows exist
    long activeShows = movie.getShows().stream()
        .filter(show -> show.getShowDateTime().isAfter(LocalDateTime.now()))
        .count();

    if (activeShows > 0) {
        log.warn("Cannot delete movie {} with {} active shows", movieId, activeShows);
        return MovieService.DeleteResult.HAS_ACTIVE_SHOWS;
    }

    movieRepository.delete(movie);
    return MovieService.DeleteResult.SUCCESS;
}

    private void mergeMovie(Movie target, Movie source) {
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setDurationMinutes(source.getDurationMinutes());
        target.setGenre(source.getGenre());
        target.setLanguage(source.getLanguage());
        target.setReleaseDate(source.getReleaseDate());
        target.setPosterUrl(source.getPosterUrl());
        target.setTrailerUrl(source.getTrailerUrl());
        target.setRating(source.getRating());
        target.setCast(source.getCast() == null ? List.of() : source.getCast());
        target.setCrew(source.getCrew() == null ? List.of() : source.getCrew());
        target.setImage(source.getImage() == null ? null : source.getImage());
    }
}
