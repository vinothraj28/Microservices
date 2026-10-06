package com.microservices.gateway.DTOS.seat;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for unlocking seats request
 * Manually unlocks seats before lock expiry
 * 
 * @param lockId The lock ID from LockSeatsResponseDTO
 */
public record UnlockSeatsRequestDTO(
        @NotBlank(message = "Lock ID is required")
        String lockId
) {
}
