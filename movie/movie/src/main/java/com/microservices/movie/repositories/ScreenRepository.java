package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, UUID> {
    List<Screen> findByTheaterId(UUID theaterId);
    Optional<Screen> findByTheaterIdAndScreenNumber(UUID theaterId, Integer screenNumber);
    boolean existsByTheaterIdAndScreenNumber(UUID theaterId, Integer screenNumber);
}
