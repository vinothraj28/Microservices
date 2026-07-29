package com.microservices.movie.configurations;

import com.microservices.movie.models.entities.SeatLock;
import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.repositories.SeatLockRepository;
import com.microservices.movie.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final SeatLockRepository seatLockRepository;
    private final BookingRepository bookingRepository;

    /**
     * Release expired seat locks every minute
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    @Transactional
    public void releaseExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        List<SeatLock> expiredLocks = seatLockRepository.findExpiredLocks(now);
        
        if (!expiredLocks.isEmpty()) {
            log.info("Releasing {} expired seat locks", expiredLocks.size());
            expiredLocks.forEach(lock -> {
                lock.setStatus("EXPIRED");
                seatLockRepository.save(lock);
            });
        }
    }

    /**
     * Expire pending bookings that haven't been paid every 2 minutes
     */
    @Scheduled(fixedRate = 120000) // Run every 2 minutes
    @Transactional
    public void expirePendingBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(BookingStatus.PENDING, now);
        
        if (!expiredBookings.isEmpty()) {
            log.info("Expiring {} pending bookings", expiredBookings.size());
            expiredBookings.forEach(booking -> {
                booking.setStatus(BookingStatus.EXPIRED);
                bookingRepository.save(booking);
                
                // Release the associated lock if exists
                if (booking.getLockId() != null) {
                    seatLockRepository.findById(booking.getLockId()).ifPresent(lock -> {
                        lock.setStatus("EXPIRED");
                        seatLockRepository.save(lock);
                    });
                }
            });
        }
    }
}
