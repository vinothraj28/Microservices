package com.microservices.gateway.DTOS.movie;

import java.util.List;

public record MovieListResponseDTO(
        List<MovieResponseDTO> movieResponseDTO,
        int totalCount,
        int page,
        int size,
        int totalPages,
        boolean hasNext
        )
{}
