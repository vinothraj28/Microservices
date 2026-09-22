package com.microservices.gateway.DTOS.show;

import com.microservices.gateway.DTOS.mfa.MfaVerificationRequestDTO;

import java.util.List;

/**
 * DTO for list of shows response
 * Includes pagination metadata
 * 
 * @param shows List of show responses
 * @param totalCount Total number of shows matching the query
 */
public record ShowListResponseDTO(
        List<ShowResponseDTO> shows,
        Integer totalCount,
        Integer totalPages,
        Boolean hasNext
) {
    public static ShowListResponseDTO from(List<ShowResponseDTO> shows, Integer totalCount, Integer totalPages, Boolean hasNext) {
        return new ShowListResponseDTO(shows, totalCount, totalPages, hasNext);
    }
    public static ShowListResponseDTO fromShowsAndTotalCount(List<ShowResponseDTO> shows, Integer totalCount) {
        return new ShowListResponseDTO(shows, totalCount, 0, false);
    }
}
