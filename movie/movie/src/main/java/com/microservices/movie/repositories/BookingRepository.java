package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Page<Booking> findByUserId(UUID userId, Pageable pageable);
    Page<Booking> findByUserIdAndStatus(UUID userId, BookingStatus status, Pageable pageable);
    List<Booking> findByShowId(UUID showId);
    List<Booking> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Booking> findByShow_IdAndStatus(UUID showId, BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.expiresAt < :expiryTime")
    List<Booking> findExpiredBookings(@Param("status") BookingStatus status, 
                                     @Param("expiryTime") LocalDateTime expiryTime);
}
