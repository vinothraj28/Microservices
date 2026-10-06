package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.payment.*;
import com.microservices.gateway.services.gRPCServices.PaymentGRPCService;
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
 * REST Controller for Payment operations
 * All endpoints are JWT-protected and require valid Bearer token
 * 
 * Endpoints:
 * - POST   /api/v1/payments                 - Initiate payment
 * - POST   /api/v1/payments/verify          - Verify payment status
 * - POST   /api/v1/payments/refund          - Process refund
 * - GET    /api/v1/payments/{paymentId}     - Get payment status
 * 
 * Design Patterns:
 * - Reactive Programming: Uses Reactor for non-blocking I/O
 * - Dependency Injection: PaymentGRPCService injected via constructor
 * - Validation: Jakarta validation on request DTOs
 * - JWT Authentication: All endpoints require valid Bearer token
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "APIs for managing ticket payments")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentGRPCService paymentGRPCService;

    @Operation(summary = "Initiate payment", 
               description = "Initiates a payment transaction for a booking")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment initiated successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "409", description = "Booking is expired or not in PENDING status")
    })
    @PostMapping
    public Mono<ResponseEntity<PaymentResponseDTO>> initiatePayment(
            @Valid @RequestBody InitiatePaymentRequestDTO request) {
        
        log.info("REST: Received request to initiate payment for booking {} with method {}", 
                request.bookingId(), request.paymentMethod());

        return Mono.fromCallable(() -> paymentGRPCService.initiatePayment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnSuccess(response -> log.info("REST: Payment initiated successfully: {}", 
                        response.getBody().paymentId()))
                .doOnError(error -> log.error("REST: Error initiating payment", error));
    }

    @Operation(summary = "Verify payment", 
               description = "Verifies the status of a payment transaction")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment status verified successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/verify")
    public Mono<ResponseEntity<PaymentResponseDTO>> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequestDTO request) {
        
        log.info("REST: Received request to verify payment {}", request.paymentId());

        return Mono.fromCallable(() -> paymentGRPCService.verifyPayment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Payment verified successfully: {}", 
                        request.paymentId()))
                .doOnError(error -> log.error("REST: Error verifying payment", error));
    }

    @Operation(summary = "Process refund", 
               description = "Initiates a refund for a completed payment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refund processed successfully",
                    content = @Content(schema = @Schema(implementation = RefundResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "409", description = "Payment cannot be refunded (invalid status)")
    })
    @PostMapping("/refund")
    public Mono<ResponseEntity<RefundResponseDTO>> processRefund(
            @RequestParam String paymentId,
            @RequestParam Double amount,
            @RequestParam(required = false) String reason) {
        
        log.info("REST: Received request to process refund for payment {} with amount {}", paymentId, amount);

        return Mono.fromCallable(() -> paymentGRPCService.processRefund(paymentId, amount, reason))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnSuccess(response -> log.info("REST: Refund processed successfully: {}", 
                        response.getBody().refundId()))
                .doOnError(error -> log.error("REST: Error processing refund", error));
    }

    @Operation(summary = "Get payment status", 
               description = "Retrieves the current status of a payment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid payment ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{paymentId}")
    public Mono<ResponseEntity<PaymentResponseDTO>> getPaymentStatus(
            @PathVariable String paymentId) {
        
        log.info("REST: Received request to get payment status {}", paymentId);

        return Mono.fromCallable(() -> paymentGRPCService.getPaymentStatus(paymentId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Payment status retrieved successfully: {}", paymentId))
                .doOnError(error -> log.error("REST: Error getting payment status", error));
    }
}
