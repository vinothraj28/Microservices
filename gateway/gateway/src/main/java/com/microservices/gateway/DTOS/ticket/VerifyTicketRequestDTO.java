package com.microservices.gateway.DTOS.ticket;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for verifying a ticket
 * 
 * @param ticketId UUID of the ticket
 * @param qrCode QR code to verify
 */
public record VerifyTicketRequestDTO(
        @NotBlank(message = "Ticket ID is required")
        String ticketId,

        @NotBlank(message = "QR code is required")
        String qrCode
) {
}
