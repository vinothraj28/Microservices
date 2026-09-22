package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Show;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShowRepository extends JpaRepository<Show, UUID>, JpaSpecificationExecutor<Show> {
    
    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.showDateTime BETWEEN :startDate AND :endDate")
    List<Show> findByMovieIdAndDateRange(@Param("movieId") UUID movieId, 
                                         @Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Show s WHERE s.screen.theater.id = :theaterId AND s.showDateTime BETWEEN :startDate AND :endDate")
    List<Show> findByTheaterIdAndDateRange(@Param("theaterId") UUID theaterId, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Show s WHERE s.screen.theater.city = :city AND s.movie.id = :movieId AND s.showDateTime BETWEEN :startDate AND :endDate")
    List<Show> findByMovieIdAndCityAndDateRange(@Param("movieId") UUID movieId, 
                                                @Param("city") String city,
                                                @Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    List<Show> findByScreen_Id(UUID screenId);
    List<Show> findByScreen_IdAndShowDateTimeBetween(UUID screenId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Show> findByMovie_IdOrderByShowDateTimeAsc(UUID movieId);
    List<Show> findByScreen_Theater_IdOrderByShowDateTimeAsc(UUID theaterId);
    List<Show> findByTheaterId(UUID theaterId);
    Page<Show> findByShowDateTimeGreaterThanEqual(LocalDateTime showDateTime, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"movie", "screen", "screen.theater"})
    Page<Show> findAll(Specification<Show> spec, Pageable pageable);

}
