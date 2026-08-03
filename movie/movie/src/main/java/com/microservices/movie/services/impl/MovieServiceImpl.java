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
    public void deleteMovie(UUID movieId) {
        log.info("Deleting movie with id: {}", movieId);
        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
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
