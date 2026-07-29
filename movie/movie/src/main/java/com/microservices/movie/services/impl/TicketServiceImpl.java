package com.microservices.movie.services.impl;

import com.microservices.movie.exceptions.BookingException;
import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.entities.Seat;
import com.microservices.movie.models.entities.SeatLock;
import com.microservices.movie.models.entities.Ticket;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.models.enums.TicketStatus;
import com.microservices.movie.repositories.BookingRepository;
import com.microservices.movie.repositories.SeatLockRepository;
import com.microservices.movie.repositories.TicketRepository;
import com.microservices.movie.services.interfaces.TicketService;
import com.microservices.movie.utils.QRCodeGenerator;
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
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final SeatLockRepository seatLockRepository;
    private final QRCodeGenerator qrCodeGenerator;

    @Override
    @Transactional
    public List<Ticket> generateTickets(UUID bookingId) {
        log.info("Generating tickets for booking {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId.toString()));
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BookingException("Tickets can only be generated for confirmed bookings");
        }

        List<Ticket> existingTickets = ticketRepository.findByBookingId(bookingId);
        if (!existingTickets.isEmpty()) {
            return existingTickets;
        }

        SeatLock seatLock = seatLockRepository.findById(booking.getLockId())
                .orElseThrow(() -> new BookingException("Seat lock for booking not found"));
        Map<UUID, Seat> seatMap = booking.getShow().getScreen().getSeats().stream()
                .collect(Collectors.toMap(Seat::getId, Function.identity()));

        List<Ticket> createdTickets = seatLock.getSeatIds().stream()
                .map(seatId -> {
                    Seat seat = seatMap.get(seatId);
                    if (seat == null) {
                        throw new BookingException("Seat " + seatId + " not found on show screen");
                    }
                    Ticket ticket = Ticket.builder()
                            .booking(booking)
                            .show(booking.getShow())
                            .seat(seat)
                            .status(TicketStatus.ISSUED)
                            .price(booking.getShow().getBasePrice() * (seat.getPriceMultiplier() == null ? 1.0D : seat.getPriceMultiplier()))
                            .issuedAt(LocalDateTime.now())
                            .qrCode("PENDING-" + UUID.randomUUID())
                            .build();
                    Ticket persistedTicket = ticketRepository.save(ticket);
                    persistedTicket.setQrCode(qrCodeGenerator.generateQRCode(buildQrPayload(persistedTicket)));
                    return ticketRepository.save(persistedTicket);
                })
                .toList();

        log.info("Generated {} tickets for booking {}", createdTickets.size(), bookingId);
        return createdTickets;
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket getTicket(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyTicket(String qrCode) {
        return ticketRepository.findByQrCode(qrCode)
                .map(Ticket::isValid)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> listTicketsByBooking(UUID bookingId) {
        log.debug("Listing tickets for booking {}", bookingId);
        return ticketRepository.findByBookingId(bookingId);
    }

    @Override
    @Transactional
    public void cancelTickets(UUID bookingId) {
        List<Ticket> tickets = ticketRepository.findByBookingId(bookingId);
        if (tickets.isEmpty()) {
            return;
        }
        log.info("Cancelling {} tickets for booking {}", tickets.size(), bookingId);
        tickets.forEach(ticket -> ticket.setStatus(TicketStatus.CANCELLED));
        ticketRepository.saveAll(tickets);
    }

    private String buildQrPayload(Ticket ticket) {
        return "ticketId=" + ticket.getId()
                + "|showId=" + ticket.getShow().getId()
                + "|seatId=" + ticket.getSeat().getId()
                + "|bookingId=" + ticket.getBooking().getId()
                + "|userId=" + ticket.getBooking().getUserId();
    }
}
