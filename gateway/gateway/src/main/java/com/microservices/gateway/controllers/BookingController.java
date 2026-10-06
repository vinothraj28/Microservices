package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.booking.*;
import com.microservices.gateway.services.gRPCServices.BookingGRPCService;
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
 * REST Controller for Booking operations
 * All endpoints are JWT-protected and require valid Bearer token
 * 
 * Endpoints:
 * - POST   /api/v1/bookings                   - Create a booking
 * - POST   /api/v1/bookings/confirm          - Confirm a booking with payment
 * - POST   /api/v1/bookings/cancel           - Cancel a booking
 * - GET    /api/v1/bookings/{bookingId}      - Get booking by ID
 * - GET    /api/v1/bookings/user/{userId}    - List user bookings
 * 
 * Design Patterns:
 * - Reactive Programming: Uses Reactor for non-blocking I/O
 * - Dependency Injection: BookingGRPCService injected via constructor
 * - Validation: Jakarta validation on request DTOs
 * - JWT Authentication: All endpoints require valid Bearer token
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "APIs for managing movie ticket bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingGRPCService bookingGRPCService;

    @Operation(summary = "Create a new booking", 
               description = "Creates a new booking for selected seats on a show")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Booking created successfully",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Show or seats not found"),
            @ApiResponse(responseCode = "409", description = "Seats already booked or locked")
    })
    @PostMapping
    public Mono<ResponseEntity<BookingResponseDTO>> createBooking(
            @Valid @RequestBody CreateBookingRequestDTO request) {
        
        log.info("REST: Received request to create booking for user {} on show {}", 
                request.userId(), request.showId());

        return Mono.fromCallable(() -> bookingGRPCService.createBooking(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnSuccess(response -> log.info("REST: Booking created successfully: {}", 
                        response.getBody().bookingId()))
                .doOnError(error -> log.error("REST: Error creating booking", error));
    }

    @Operation(summary = "Confirm a booking with payment", 
               description = "Confirms a pending booking after successful payment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking confirmed successfully",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking or payment not found"),
            @ApiResponse(responseCode = "409", description = "Booking cannot be confirmed (invalid status)")
    })
    @PostMapping("/confirm")
    public Mono<ResponseEntity<BookingResponseDTO>> confirmBooking(
            @Valid @RequestBody ConfirmBookingRequestDTO request) {
        
        log.info("REST: Received request to confirm booking {}", request.bookingId());

        return Mono.fromCallable(() -> bookingGRPCService.confirmBooking(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Booking confirmed successfully: {}", 
                        request.bookingId()))
                .doOnError(error -> log.error("REST: Error confirming booking", error));
    }

    @Operation(summary = "Cancel a booking", 
               description = "Cancels a pending or confirmed booking and initiates refund")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking cancelled successfully",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "409", description = "Booking cannot be cancelled (already cancelled)")
    })
    @PostMapping("/cancel")
    public Mono<ResponseEntity<BookingResponseDTO>> cancelBooking(
            @Valid @RequestBody CancelBookingRequestDTO request) {
        
        log.info("REST: Received request to cancel booking {}", request.bookingId());

        return Mono.fromCallable(() -> bookingGRPCService.cancelBooking(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Booking cancelled successfully: {}", 
                        request.bookingId()))
                .doOnError(error -> log.error("REST: Error cancelling booking", error));
    }

    @Operation(summary = "Get booking by ID", 
               description = "Retrieves complete details of a booking including show and seat information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{bookingId}")
    public Mono<ResponseEntity<BookingResponseDTO>> getBooking(
            @PathVariable String bookingId) {
        
        log.info("REST: Received request to get booking {}", bookingId);

        return Mono.fromCallable(() -> bookingGRPCService.getBooking(bookingId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Booking retrieved successfully: {}", bookingId))
                .doOnError(error -> log.error("REST: Error getting booking {}", bookingId, error));
    }

    @Operation(summary = "List user bookings", 
               description = "Lists all bookings for a specific user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bookings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ListBookingsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid user ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    })
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<ListBookingsResponseDTO>> listUserBookings(
            @PathVariable String userId) {
        
        log.info("REST: Received request to list bookings for user {}", userId);

        return Mono.fromCallable(() -> bookingGRPCService.listUserBookings(userId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Found {} bookings for user {}", 
                        response.getBody().totalCount(), userId))
                .doOnError(error -> log.error("REST: Error listing bookings for user {}", userId, error));
    }
}
