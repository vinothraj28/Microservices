package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.ticket.*;
import com.microservices.gateway.services.gRPCServices.TicketGRPCService;
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

import java.util.List;

/**
 * REST Controller for Ticket operations
 * All endpoints are JWT-protected and require valid Bearer token
 * 
 * Endpoints:
 * - POST   /api/v1/tickets/generate         - Generate tickets for a booking
 * - GET    /api/v1/tickets/{ticketId}       - Get ticket details
 * - POST   /api/v1/tickets/verify           - Verify ticket validity
 * - GET    /api/v1/tickets/booking/{bookingId} - List tickets for a booking
 * - POST   /api/v1/tickets/cancel           - Cancel tickets for a booking
 * 
 * Design Patterns:
 * - Reactive Programming: Uses Reactor for non-blocking I/O
 * - Dependency Injection: TicketGRPCService injected via constructor
 * - Validation: Jakarta validation on request DTOs
 * - JWT Authentication: All endpoints require valid Bearer token
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "APIs for managing movie tickets")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketGRPCService ticketGRPCService;

    @Operation(summary = "Generate tickets", 
               description = "Generates tickets for all seats in a confirmed booking")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tickets generated successfully",
                    content = @Content(schema = @Schema(implementation = ListTicketsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "409", description = "Booking is not in CONFIRMED status")
    })
    @PostMapping("/generate")
    public Mono<ResponseEntity<ListTicketsResponseDTO>> generateTickets(
            @Valid @RequestBody GenerateTicketsRequestDTO request) {
        
        log.info("REST: Received request to generate tickets for booking {}", request.bookingId());

        return Mono.fromCallable(() -> {
                    List<TicketResponseDTO> tickets = ticketGRPCService.generateTickets(request);
                    return new ListTicketsResponseDTO(tickets);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnSuccess(response -> log.info("REST: Tickets generated successfully for booking {}", 
                        request.bookingId()))
                .doOnError(error -> log.error("REST: Error generating tickets", error));
    }

    @Operation(summary = "Get ticket details", 
               description = "Retrieves complete details of a ticket including QR code and seat information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ticket ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("/{ticketId}")
    public Mono<ResponseEntity<TicketResponseDTO>> getTicket(
            @PathVariable String ticketId) {
        
        log.info("REST: Received request to get ticket {}", ticketId);

        return Mono.fromCallable(() -> ticketGRPCService.getTicket(ticketId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Ticket retrieved successfully: {}", ticketId))
                .doOnError(error -> log.error("REST: Error getting ticket", error));
    }

    @Operation(summary = "Verify ticket", 
               description = "Verifies a ticket's validity using QR code (used at theater entry)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket verified successfully"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "409", description = "Ticket is invalid or expired")
    })
    @PostMapping("/verify")
    public Mono<ResponseEntity<String>> verifyTicket(
            @Valid @RequestBody VerifyTicketRequestDTO request) {
        
        log.info("REST: Received request to verify ticket");

        return Mono.fromCallable(() -> {
                    boolean isValid = ticketGRPCService.verifyTicket(request);
                    return isValid ? "Ticket verified successfully" : "Ticket verification failed";
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Ticket verified successfully"))
                .doOnError(error -> log.error("REST: Error verifying ticket", error));
    }

    @Operation(summary = "List tickets for booking", 
               description = "Lists all tickets generated for a specific booking")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ListTicketsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/booking/{bookingId}")
    public Mono<ResponseEntity<ListTicketsResponseDTO>> listTicketsByBooking(
            @PathVariable String bookingId) {
        
        log.info("REST: Received request to list tickets for booking {}", bookingId);

        return Mono.fromCallable(() -> ticketGRPCService.listTicketsByBooking(bookingId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Found {} tickets for booking {}", 
                        response.getBody().tickets().size(), bookingId))
                .doOnError(error -> log.error("REST: Error listing tickets for booking", error));
    }

    @Operation(summary = "Cancel tickets", 
               description = "Cancels all tickets for a specific booking (used when booking is cancelled)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets cancelled successfully"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "409", description = "Tickets cannot be cancelled (invalid status)")
    })
    @PostMapping("/cancel/{bookingId}")
    public Mono<ResponseEntity<String>> cancelTickets(
            @PathVariable String bookingId) {
        
        log.info("REST: Received request to cancel tickets for booking {}", bookingId);

        return Mono.fromCallable(() -> {
                    ticketGRPCService.cancelTickets(bookingId);
                    return "Successfully cancelled tickets";
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Tickets cancelled successfully for booking {}", 
                        bookingId))
                .doOnError(error -> log.error("REST: Error cancelling tickets", error));
    }
}
