package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    List<Ticket> generateTickets(UUID bookingId);

    Ticket getTicket(UUID ticketId);

    boolean verifyTicket(String qrCode);

    List<Ticket> listTicketsByBooking(UUID bookingId);

    void cancelTickets(UUID bookingId);
}
