package com.microservices.movie.services.grpcServices;

import com.microservices.movie.grpc.*;
import com.microservices.movie.models.entities.Ticket;
import com.microservices.movie.services.interfaces.TicketService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for Ticket operations
 * Handles GenerateTickets, GetTicket, VerifyTicket, ListTicketsByBooking, and CancelTickets RPC calls
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class TicketGrpcService extends TicketServiceGrpc.TicketServiceImplBase {

    private final TicketService ticketService;

    @Override
    @Transactional
    public void generateTickets(GenerateTicketsRequest request, StreamObserver<GenerateTicketsResponse> responseObserver) {
        log.info("gRPC: Generating tickets for booking {}", request.getBookingId());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());
            List<Ticket> tickets = ticketService.generateTickets(bookingId);

            List<TicketResponse> ticketResponses = tickets.stream()
                    .map(this::mapTicketToResponse)
                    .collect(Collectors.toList());

            GenerateTicketsResponse response = GenerateTicketsResponse.newBuilder()
                    .addAllTickets(ticketResponses)
                    .setBookingId(bookingId.toString())
                    .setTicketCount(ticketResponses.size())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in GenerateTickets request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error generating tickets", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void getTicket(GetTicketRequest request, StreamObserver<TicketResponse> responseObserver) {
        log.info("gRPC: Getting ticket {}", request.getTicketId());
        
        try {
            UUID ticketId = UUID.fromString(request.getTicketId());
            Ticket ticket = ticketService.getTicket(ticketId);

            TicketResponse response = mapTicketToResponse(ticket);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in GetTicket request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error getting ticket", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void verifyTicket(VerifyTicketRequest request, StreamObserver<VerifyTicketResponse> responseObserver) {
        log.info("gRPC: Verifying ticket {} with QR code", request.getTicketId());
        
        try {
            UUID ticketId = UUID.fromString(request.getTicketId());
            Ticket ticket = ticketService.verifyTicket(ticketId, request.getQrCode());

            VerifyTicketResponse response = VerifyTicketResponse.newBuilder()
                    .setValid(ticket.isValid())
                    .setMessage("Ticket verified successfully")
                    .setTicket(mapTicketToResponse(ticket))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in VerifyTicket request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error verifying ticket", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void listTicketsByBooking(ListTicketsByBookingRequest request, StreamObserver<ListTicketsResponse> responseObserver) {
        log.info("gRPC: Listing tickets for booking {}", request.getBookingId());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());
            List<Ticket> tickets = ticketService.listTicketsByBooking(bookingId);

            List<TicketResponse> ticketResponses = tickets.stream()
                    .map(this::mapTicketToResponse)
                    .collect(Collectors.toList());

            ListTicketsResponse response = ListTicketsResponse.newBuilder()
                    .addAllTickets(ticketResponses)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in ListTicketsByBooking request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error listing tickets by booking", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void cancelTickets(CancelTicketsRequest request, StreamObserver<CancelTicketsResponse> responseObserver) {
        log.info("gRPC: Cancelling tickets for booking {}", request.getBookingId());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());
            int cancelledCount = ticketService.cancelTickets(bookingId);

            CancelTicketsResponse response = CancelTicketsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Tickets cancelled successfully")
                    .setTicketsCancelled(cancelledCount)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in CancelTickets request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error cancelling tickets", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    private TicketResponse mapTicketToResponse(Ticket ticket) {
        return TicketResponse.newBuilder()
                .setTicketId(ticket.getId().toString())
                .setBookingId(ticket.getBooking().getId().toString())
                .setSeatId(ticket.getSeat().getId().toString())
                .setQrCode(ticket.getQrCode())
                .setStatus(ticket.getStatus().toString())
                .setPrice(ticket.getPrice())
                .setIssuedAt(ticket.getIssuedAt() != null ? convertToTimestamp(ticket.getIssuedAt()) : com.google.protobuf.Timestamp.getDefaultInstance())
                .setUsedAt(ticket.getUsedAt() != null ? convertToTimestamp(ticket.getUsedAt()) : com.google.protobuf.Timestamp.getDefaultInstance())
                .build();
    }

    private com.google.protobuf.Timestamp convertToTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return com.google.protobuf.Timestamp.getDefaultInstance();
        }
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        return com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
