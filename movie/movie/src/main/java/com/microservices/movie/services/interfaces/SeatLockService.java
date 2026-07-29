package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.SeatLock;
import com.microservices.movie.models.enums.SeatStatus;

import java.util.List;
import java.util.UUID;

public interface SeatLockService {

    SeatLock lockSeats(UUID showId, UUID userId, List<UUID> seatIds);

    void unlockSeats(UUID lockId, UUID userId);

    SeatStatus getSeatStatus(UUID showId, UUID seatId);
}
