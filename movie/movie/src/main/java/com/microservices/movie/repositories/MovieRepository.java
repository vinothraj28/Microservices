package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    Page<Movie> findByGenre(String genre, Pageable pageable);
    Page<Movie> findByLanguage(String language, Pageable pageable);
    Page<Movie> findByGenreAndLanguage(String genre, String language, Pageable pageable);
}
