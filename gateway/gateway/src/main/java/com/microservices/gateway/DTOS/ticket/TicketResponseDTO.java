package com.microservices.gateway.DTOS.ticket;

import com.microservices.gateway.DTOS.show.ShowResponseDTO;
import com.microservices.gateway.DTOS.show.SeatInfoDTO;
import java.time.LocalDateTime;

/**
 * DTO for ticket response
 * Contains complete ticket information including seat and show details
 * 
 * @param ticketId Unique ticket identifier
 * @param bookingId ID of the associated booking
 * @param seatId ID of the seat
 * @param seat Seat details
 * @param show Show details
 * @param qrCode QR code for ticket verification
 * @param status Ticket status (ISSUED, USED, CANCELLED)
 * @param price Ticket price
 * @param issuedAt When ticket was issued
 * @param usedAt When ticket was used (scanned)
 */
public record TicketResponseDTO(
        String ticketId,
        String bookingId,
        String seatId,
        SeatInfoDTO seat,
        ShowResponseDTO show,
        String qrCode,
        String status,
        Double price,
        LocalDateTime issuedAt,
        LocalDateTime usedAt
) {
}
