package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Ticket;
import com.microservices.movie.models.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByBookingId(UUID bookingId);
    Optional<Ticket> findByQrCode(String qrCode);
    List<Ticket> findByShowId(UUID showId);
    List<Ticket> findByShowIdAndStatus(UUID showId, TicketStatus status);
}
