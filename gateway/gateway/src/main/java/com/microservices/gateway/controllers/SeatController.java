package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.seat.*;
import com.microservices.gateway.services.gRPCServices.SeatGRPCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * REST Controller for Seat operations
 * All endpoints are JWT-protected and require valid Bearer token
 * 
 * Endpoints:
 * - POST   /api/v1/seats/lock          - Lock seats for 5 minutes
 * - POST   /api/v1/seats/unlock        - Unlock seats manually
 * - POST   /api/v1/seats/status        - Check seat availability
 * 
 * Design Patterns:
 * - Reactive Programming: Uses Reactor for non-blocking I/O
 * - Dependency Injection: SeatGRPCService injected via constructor
 * - Validation: Jakarta validation on request DTOs
 * - JWT Authentication: All endpoints require valid Bearer token
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
@Tag(name = "Seat Management", description = "APIs for managing seat locking and availability")
@SecurityRequirement(name = "bearerAuth")
public class SeatController {

    private final SeatGRPCService seatGRPCService;

    @Operation(summary = "Lock seats", 
               description = "Locks selected seats for 5 minutes. Returns lockId required for booking.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Seats locked successfully",
                    content = @Content(schema = @Schema(implementation = LockSeatsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Show not found"),
            @ApiResponse(responseCode = "409", description = "Some or all seats already locked/booked")
    })
    @PostMapping("/lock")
    public Mono<ResponseEntity<LockSeatsResponseDTO>> lockSeats(
            @Valid @RequestBody LockSeatsRequestDTO request) {
        
        log.info("REST: Received request to lock {} seats for user {} on show {}", 
                request.seatIds().size(), request.userId(), request.showId());

        return Mono.fromCallable(() -> seatGRPCService.lockSeats(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> {
                    if (response.success()) {
                        log.info("REST: Seats locked successfully with lock ID {}", response.lockId());
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        log.warn("REST: Failed to lock seats: {}", response.message());
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                    }
                })
                .doOnError(error -> log.error("REST: Error locking seats", error));
    }

    @Operation(summary = "Unlock seats", 
               description = "Manually unlocks seats before lock expiry (5 minutes). Optional operation.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seats unlocked successfully"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid lock ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Lock not found")
    })
    @PostMapping("/unlock")
    public Mono<ResponseEntity<String>> unlockSeats(
            @Valid @RequestBody UnlockSeatsRequestDTO request) {
        
        log.info("REST: Received request to unlock seats with lock ID {}", request.lockId());

        return Mono.fromCallable(() -> seatGRPCService.unlockSeats(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(success -> {
                    if (success) {
                        log.info("REST: Seats unlocked successfully for lock {}", request.lockId());
                        return ResponseEntity.ok("Seats unlocked successfully");
                    } else {
                        log.warn("REST: Failed to unlock seats for lock {}", request.lockId());
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to unlock seats");
                    }
                })
                .doOnError(error -> log.error("REST: Error unlocking seats", error));
    }

    @Operation(summary = "Check seat availability", 
               description = "Checks the current status of specific seats (AVAILABLE, LOCKED, BOOKED)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seat status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SeatStatusResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Show not found")
    })
    @PostMapping("/status")
    public Mono<ResponseEntity<SeatStatusResponseDTO>> getSeatStatus(
            @Valid @RequestBody GetSeatStatusRequestDTO request) {
        
        log.info("REST: Received request to get status for {} seats on show {}", 
                request.seatIds().size(), request.showId());

        return Mono.fromCallable(() -> seatGRPCService.getSeatStatus(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Seat status retrieved successfully for show {}", 
                        request.showId()))
                .doOnError(error -> log.error("REST: Error getting seat status", error));
    }
}
