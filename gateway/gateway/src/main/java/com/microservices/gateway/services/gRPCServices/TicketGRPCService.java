package com.microservices.gateway.services.gRPCServices;

import com.google.protobuf.Timestamp;
import com.microservices.gateway.DTOS.ticket.*;
import com.microservices.movie.grpc.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gateway gRPC client service for Ticket operations
 * Handles communication between Gateway REST API and Movie Service gRPC server
 * 
 * Design Pattern: Adapter Pattern - Converts DTOs to gRPC proto messages
 * Error Handling: Maps gRPC status codes to domain exceptions
 */
@Slf4j
@Service
public class TicketGRPCService {

    @GrpcClient("movie-service")
    private TicketServiceGrpc.TicketServiceBlockingStub ticketServiceStub;

    public List<TicketResponseDTO> generateTickets(GenerateTicketsRequestDTO request) {
        log.info("Gateway gRPC: Generating tickets for booking {}", request.bookingId());

        try {
            GenerateTicketsRequest grpcRequest = GenerateTicketsRequest.newBuilder()
                    .setBookingId(request.bookingId())
                    .build();

            GenerateTicketsResponse grpcResponse = ticketServiceStub.generateTickets(grpcRequest);
            
            return grpcResponse.getTicketsList().stream()
                    .map(this::mapToTicketResponseDTO)
                    .collect(Collectors.toList());
        } catch (StatusRuntimeException e) {
            log.error("gRPC error generating tickets: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to generate tickets: " + e.getStatus().getDescription());
        }
    }

    public TicketResponseDTO getTicket(String ticketId) {
        log.info("Gateway gRPC: Getting ticket {}", ticketId);

        try {
            GetTicketRequest grpcRequest = GetTicketRequest.newBuilder()
                    .setTicketId(ticketId)
                    .build();

            TicketResponse grpcResponse = ticketServiceStub.getTicket(grpcRequest);
            return mapToTicketResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error getting ticket: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to get ticket: " + e.getStatus().getDescription());
        }
    }

    public boolean verifyTicket(VerifyTicketRequestDTO request) {
        log.info("Gateway gRPC: Verifying ticket with QR code");

        try {
            VerifyTicketRequest grpcRequest = VerifyTicketRequest.newBuilder()
                    .setQrCode(request.qrCode())
                    .build();

            VerifyTicketResponse grpcResponse = ticketServiceStub.verifyTicket(grpcRequest);
            
            if (!grpcResponse.getValid()) {
                throw new RuntimeException("Ticket is invalid or expired");
            }
            
            return true;
        } catch (StatusRuntimeException e) {
            log.error("gRPC error verifying ticket: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to verify ticket: " + e.getStatus().getDescription());
        }
    }

    public ListTicketsResponseDTO listTicketsByBooking(String bookingId) {
        log.info("Gateway gRPC: Listing tickets for booking {}", bookingId);

        try {
            ListTicketsByBookingRequest grpcRequest = ListTicketsByBookingRequest.newBuilder()
                    .setBookingId(bookingId)
                    .build();

            ListTicketsResponse grpcResponse = ticketServiceStub.listTicketsByBooking(grpcRequest);
            
            List<TicketResponseDTO> tickets = grpcResponse.getTicketsList().stream()
                    .map(this::mapToTicketResponseDTO)
                    .collect(Collectors.toList());

            return new ListTicketsResponseDTO(tickets);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error listing tickets by booking: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to list tickets: " + e.getStatus().getDescription());
        }
    }

    public void cancelTickets(String bookingId) {
        log.info("Gateway gRPC: Cancelling tickets for booking {}", bookingId);

        try {
            CancelTicketsRequest grpcRequest = CancelTicketsRequest.newBuilder()
                    .setBookingId(bookingId)
                    .build();

            CancelTicketsResponse grpcResponse = ticketServiceStub.cancelTickets(grpcRequest);
            
            if (!grpcResponse.getSuccess()) {
                throw new RuntimeException(grpcResponse.getMessage());
            }
        } catch (StatusRuntimeException e) {
            log.error("gRPC error cancelling tickets: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to cancel tickets: " + e.getStatus().getDescription());
        }
    }

    private TicketResponseDTO mapToTicketResponseDTO(TicketResponse response) {
        return new TicketResponseDTO(
                response.getTicketId(),
                response.getBookingId(),
                response.getSeatId(),
                null,
                null,
                response.getQrCode(),
                response.getStatus(),
                response.getPrice(),
                convertTimestampToLocalDateTime(response.getIssuedAt()),
                convertTimestampToLocalDateTime(response.getUsedAt())
        );
    }

    private LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        if (timestamp == null || timestamp.getSeconds() == 0) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
