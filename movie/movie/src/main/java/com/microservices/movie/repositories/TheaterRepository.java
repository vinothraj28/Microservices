package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, UUID> {
    Page<Theater> findByCity(String city, Pageable pageable);
    List<Theater> findByCity(String city);
    boolean existsByNameAndCity(String name, String city);

    Page<Theater> findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String name,
            String city,
            String address,
            Pageable pageable
    );
}
