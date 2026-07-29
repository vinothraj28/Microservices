package com.microservices.movie.services.impl;

import com.microservices.movie.configurations.MovieConfig;
import com.microservices.movie.exceptions.BookingException;
import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.entities.Payment;
import com.microservices.movie.models.entities.Seat;
import com.microservices.movie.models.entities.SeatLock;
import com.microservices.movie.models.entities.Show;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.models.enums.PaymentStatus;
import com.microservices.movie.repositories.BookingRepository;
import com.microservices.movie.repositories.PaymentRepository;
import com.microservices.movie.repositories.SeatLockRepository;
import com.microservices.movie.repositories.ShowRepository;
import com.microservices.movie.services.interfaces.BookingService;
import com.microservices.movie.services.interfaces.PaymentService;
import com.microservices.movie.services.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final SeatLockRepository seatLockRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final MovieConfig movieConfig;

    @Override
    @Transactional
    public Booking createBooking(UUID showId, UUID userId, UUID lockId, String email, String phone) {
        log.info("Creating booking for user {} on show {}", userId, showId);
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", showId.toString()));
        SeatLock seatLock = seatLockRepository.findByIdForUpdate(lockId)
                .orElseThrow(() -> new ResourceNotFoundException("SeatLock", lockId.toString()));

        validateLockOwnership(showId, userId, seatLock);

        Booking booking = Booking.builder()
                .userId(userId)
                .show(show)
                .email(email)
                .phone(phone)
                .status(BookingStatus.PENDING)
                .lockId(lockId)
                .expiresAt(seatLock.getLockExpiry())
                .totalAmount(calculateTotalAmount(show, seatLock))
                .build();
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking confirmBooking(UUID bookingId, UUID paymentId) {
        log.info("Confirming booking {} using payment {}", bookingId, paymentId);
        Booking booking = getBooking(bookingId);
        ensureBookingCanBeConfirmed(booking);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId.toString()));
        if (!payment.getBooking().getId().equals(bookingId)) {
            throw new BookingException("Payment does not belong to the booking");
        }
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BookingException("Booking cannot be confirmed until payment succeeds");
        }

        SeatLock seatLock = getSeatLockForBooking(booking);
        seatLock.setStatus("CONVERTED");
        seatLockRepository.save(seatLock);

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setExpiresAt(null);
        Booking confirmedBooking = bookingRepository.save(booking);
        log.info("Booking {} confirmed successfully", bookingId);
        return confirmedBooking;
    }

    @Override
    @Transactional
    public Booking cancelBooking(UUID bookingId, String cancellationReason) {
        log.info("Cancelling booking {} with reason {}", bookingId, cancellationReason);
        Booking booking = getBooking(bookingId);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Booking is already cancelled");
        }
        if (booking.getStatus() == BookingStatus.EXPIRED) {
            throw new BookingException("Expired bookings cannot be cancelled");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            LocalDateTime cancellationDeadline = booking.getShow().getShowDateTime()
                    .minusHours(movieConfig.getBooking().getCancellationWindowHours());
            if (LocalDateTime.now().isAfter(cancellationDeadline)) {
                throw new BookingException("Booking is outside the allowed cancellation window");
            }
        }

        paymentRepository.findByBookingId(bookingId)
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCESS)
                .ifPresent(payment -> paymentService.processRefund(bookingId, cancellationReason));

        if (booking.getLockId() != null) {
            seatLockRepository.findByIdForUpdate(booking.getLockId()).ifPresent(lock -> {
                lock.setStatus("RELEASED");
                seatLockRepository.save(lock);
            });
        }

        ticketService.cancelTickets(bookingId);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setExpiresAt(null);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBooking(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> listUserBookings(UUID userId) {
        log.debug("Listing bookings for user {}", userId);
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    private void validateLockOwnership(UUID showId, UUID userId, SeatLock seatLock) {
        if (!seatLock.getShowId().equals(showId)) {
            throw new BookingException("Seat lock does not belong to the requested show");
        }
        if (!seatLock.getUserId().equals(userId)) {
            throw new BookingException("Seat lock does not belong to the requesting user");
        }
        if (!seatLock.isActive()) {
            if (seatLock.isExpired()) {
                seatLock.setStatus("EXPIRED");
            }
            throw new BookingException("Seat lock is no longer active");
        }
    }

    private void ensureBookingCanBeConfirmed(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new BookingException("Booking is already confirmed");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Cancelled bookings cannot be confirmed");
        }
        if (booking.getStatus() == BookingStatus.EXPIRED || booking.isExpired()) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new BookingException("Booking has expired");
        }
    }

    private SeatLock getSeatLockForBooking(Booking booking) {
        if (booking.getLockId() == null) {
            throw new BookingException("Booking does not have a seat lock");
        }
        return seatLockRepository.findByIdForUpdate(booking.getLockId())
                .orElseThrow(() -> new BookingException("Seat lock for booking not found"));
    }

    private double calculateTotalAmount(Show show, SeatLock seatLock) {
        Map<UUID, Seat> seatMap = show.getScreen().getSeats().stream()
                .collect(Collectors.toMap(Seat::getId, Function.identity()));
        return seatLock.getSeatIds().stream()
                .map(seatId -> {
                    Seat seat = seatMap.get(seatId);
                    if (seat == null) {
                        throw new BookingException("Seat " + seatId + " does not belong to the show's screen");
                    }
                    return show.getBasePrice() * (seat.getPriceMultiplier() == null ? 1.0D : seat.getPriceMultiplier());
                })
                .reduce(0.0D, Double::sum);
    }
}
