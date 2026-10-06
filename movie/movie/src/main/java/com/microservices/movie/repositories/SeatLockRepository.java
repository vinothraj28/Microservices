package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.SeatLock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatLockRepository extends JpaRepository<SeatLock, UUID> {

    @Modifying
    @Query("DELETE FROM SeatLock sl WHERE sl.status = 'EXPIRED' OR (sl.status = 'RELEASED' AND sl.lockExpiry < :cutoffTime)")
    int deleteExpiredAndReleasedLocks(@Param("cutoffTime") LocalDateTime cutoffTime);

    @Query("SELECT sl FROM SeatLock sl WHERE sl.showId = :showId AND sl.status = 'ACTIVE' AND sl.lockExpiry > :now")
    List<SeatLock> findActiveLocksForShow(@Param("showId") UUID showId, @Param("now") LocalDateTime now);
    
    @Query("SELECT sl FROM SeatLock sl WHERE sl.status = 'ACTIVE' AND sl.lockExpiry < :expiryTime")
    List<SeatLock> findExpiredLocks(@Param("expiryTime") LocalDateTime expiryTime);
    
    @Query("SELECT sl FROM SeatLock sl WHERE sl.showId = :showId AND sl.userId = :userId AND sl.status = 'ACTIVE' ORDER BY sl.id DESC")
    Optional<SeatLock> findByShowIdAndUserId(@Param("showId") UUID showId, @Param("userId") UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sl FROM SeatLock sl WHERE sl.id = :lockId")
    Optional<SeatLock> findByIdForUpdate(@Param("lockId") UUID lockId);
    
    @Query("SELECT sl FROM SeatLock sl JOIN sl.seatIds seatId WHERE sl.showId = :showId AND seatId IN :seatIds AND sl.status = 'ACTIVE' AND sl.lockExpiry > :now")
    List<SeatLock> findActiveLocksForSeats(@Param("showId") UUID showId, 
                                           @Param("seatIds") List<UUID> seatIds, 
                                           @Param("now") LocalDateTime now);
}
