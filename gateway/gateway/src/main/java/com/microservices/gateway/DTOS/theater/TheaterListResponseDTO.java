package com.microservices.gateway.DTOS.theater;

import java.util.List;

public record TheaterListResponseDTO(
        List<TheaterResponseDTO> theaters,
        int totalCount,
        int page,
        int size,
        int totalPages,
        boolean hasNext
) {
}
