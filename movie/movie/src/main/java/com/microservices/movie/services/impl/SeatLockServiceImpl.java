package com.microservices.movie.services.impl;

import com.microservices.movie.configurations.MovieConfig;
import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.exceptions.SeatLockException;
import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.entities.Seat;
import com.microservices.movie.models.entities.SeatLock;
import com.microservices.movie.models.entities.Show;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.models.enums.SeatStatus;
import com.microservices.movie.repositories.BookingRepository;
import com.microservices.movie.repositories.SeatLockRepository;
import com.microservices.movie.repositories.SeatRepository;
import com.microservices.movie.repositories.ShowRepository;
import com.microservices.movie.services.interfaces.SeatLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatLockServiceImpl implements SeatLockService {

    private final SeatLockRepository seatLockRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final MovieConfig movieConfig;

    @Override
    @Transactional
    public SeatLock lockSeats(UUID showId, UUID userId, List<UUID> seatIds) {
        log.info("Locking {} seats for user {} and show {}", seatIds.size(), userId, showId);
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", showId.toString()));
        List<Seat> seats = seatRepository.findAllByIdInForUpdate(seatIds);
        validateSeatsBelongToShow(show, seatIds, seats);

        LocalDateTime now = LocalDateTime.now();
        Set<UUID> bookedSeatIds = getBookedSeatIds(showId);
        if (seatIds.stream().anyMatch(bookedSeatIds::contains)) {
            throw new SeatLockException("One or more seats are already booked");
        }

        List<SeatLock> activeLocks = seatLockRepository.findActiveLocksForSeats(showId, seatIds, now);
        activeLocks.stream()
                .filter(lock -> lock.isExpired())
                .forEach(lock -> lock.setStatus("EXPIRED"));

        boolean unavailable = activeLocks.stream()
                .filter(SeatLock::isActive)
                .anyMatch(lock -> !lock.getUserId().equals(userId));
        if (unavailable) {
            throw new SeatLockException("One or more seats are already locked");
        }

        seatLockRepository.findByShowIdAndUserId(showId, userId)
                .filter(SeatLock::isActive)
                .ifPresent(existingLock -> {
                    existingLock.setStatus("RELEASED");
                    seatLockRepository.save(existingLock);
                });

        SeatLock seatLock = SeatLock.builder()
                .showId(showId)
                .userId(userId)
                .seatIds(List.copyOf(seatIds))
                .lockExpiry(now.plusMinutes(movieConfig.getSeatLock().getDurationMinutes()))
                .status("ACTIVE")
                .build();
        return seatLockRepository.save(seatLock);
    }

    @Override
    @Transactional
    public void unlockSeats(UUID lockId, UUID userId) {
        log.info("Unlocking seat lock {} for user {}", lockId, userId);
        SeatLock seatLock = seatLockRepository.findByIdForUpdate(lockId)
                .orElseThrow(() -> new ResourceNotFoundException("SeatLock", lockId.toString()));
        if (!seatLock.getUserId().equals(userId)) {
            throw new SeatLockException("Seat lock does not belong to the requesting user");
        }
        if ("CONVERTED".equals(seatLock.getStatus())) {
            throw new SeatLockException("Cannot unlock seats that were already converted to a booking");
        }
        seatLock.setStatus("RELEASED");
        seatLockRepository.save(seatLock);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatStatus getSeatStatus(UUID showId, UUID seatId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", showId.toString()));
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId.toString()));
        if (!seat.getScreen().getId().equals(show.getScreen().getId())) {
            throw new SeatLockException("Seat does not belong to the provided show");
        }

        if (getBookedSeatIds(showId).contains(seatId)) {
            return SeatStatus.BOOKED;
        }
        boolean locked = seatLockRepository.findActiveLocksForSeats(showId, List.of(seatId), LocalDateTime.now())
                .stream()
                .anyMatch(SeatLock::isActive);
        return locked ? SeatStatus.LOCKED : SeatStatus.AVAILABLE;
    }

    private void validateSeatsBelongToShow(Show show, List<UUID> requestedSeatIds, List<Seat> seats) {
        if (seats.size() != requestedSeatIds.size()) {
            throw new SeatLockException("One or more seats were not found");
        }
        boolean invalidSeat = seats.stream().anyMatch(seat -> !seat.getScreen().getId().equals(show.getScreen().getId()));
        if (invalidSeat) {
            throw new SeatLockException("All seats must belong to the show's screen");
        }
    }

    private Set<UUID> getBookedSeatIds(UUID showId) {
        Set<UUID> bookedSeatIds = new HashSet<>();
        List<Booking> confirmedBookings = bookingRepository.findByShow_IdAndStatus(showId, BookingStatus.CONFIRMED);
        for (Booking booking : confirmedBookings) {
            if (booking.getLockId() == null) {
                continue;
            }
            seatLockRepository.findById(booking.getLockId())
                    .ifPresent(lock -> bookedSeatIds.addAll(lock.getSeatIds()));
        }
        return bookedSeatIds;
    }
}
