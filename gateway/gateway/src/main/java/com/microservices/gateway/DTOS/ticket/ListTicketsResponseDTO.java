package com.microservices.gateway.DTOS.ticket;

import java.util.List;

/**
 * DTO for list of tickets response
 * 
 * @param tickets List of ticket responses
 */
public record ListTicketsResponseDTO(
        List<TicketResponseDTO> tickets
) {
}
