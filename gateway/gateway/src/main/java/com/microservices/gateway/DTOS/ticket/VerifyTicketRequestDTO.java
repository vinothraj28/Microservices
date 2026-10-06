package com.microservices.gateway.DTOS.ticket;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for verifying a ticket
 * 
 * @param qrCode QR code to verify
 */
public record VerifyTicketRequestDTO(
        @NotBlank(message = "QR code is required")
        String qrCode
) {
}
