package com.microservices.gateway.services.gRPCServices;

import com.google.protobuf.Timestamp;
import com.microservices.gateway.DTOS.booking.*;
import com.microservices.gateway.DTOS.show.SeatInfoDTO;
import com.microservices.gateway.DTOS.show.ShowResponseDTO;
import com.microservices.movie.grpc.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gateway gRPC client service for Booking operations
 * Handles communication between Gateway REST API and Movie Service gRPC server
 * 
 * Design Pattern: Adapter Pattern - Converts DTOs to gRPC proto messages
 * Error Handling: Maps gRPC status codes to domain exceptions
 */
@Slf4j
@Service
public class BookingGRPCService {

    @GrpcClient("movie-service")
    private BookingServiceGrpc.BookingServiceBlockingStub bookingServiceStub;

    public BookingResponseDTO createBooking(CreateBookingRequestDTO request) {
        log.info("Gateway gRPC: Creating booking for user {} on show {}", request.userId(), request.showId());

        try {
            CreateBookingRequest grpcRequest = CreateBookingRequest.newBuilder()
                    .setUserId(request.userId())
                    .setShowId(request.showId())
                    .addAllSeatIds(request.seatIds())
                    .setEmail(request.email())
                    .setPhone(request.phone() != null ? request.phone() : "")
                    .setLockId(request.lockId())
                    .build();

            BookingResponse grpcResponse = bookingServiceStub.createBooking(grpcRequest);
            return mapToBookingResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error creating booking: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to create booking: " + e.getStatus().getDescription());
        }
    }

    public BookingResponseDTO confirmBooking(ConfirmBookingRequestDTO request) {
        log.info("Gateway gRPC: Confirming booking {} with payment {}", request.bookingId(), request.paymentId());

        try {
            ConfirmBookingRequest grpcRequest = ConfirmBookingRequest.newBuilder()
                    .setBookingId(request.bookingId())
                    .setPaymentId(request.paymentId())
                    .build();

            BookingResponse grpcResponse = bookingServiceStub.confirmBooking(grpcRequest);
            return mapToBookingResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error confirming booking: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to confirm booking: " + e.getStatus().getDescription());
        }
    }

    public BookingResponseDTO cancelBooking(CancelBookingRequestDTO request) {
        log.info("Gateway gRPC: Cancelling booking {}", request.bookingId());

        try {
            CancelBookingRequest grpcRequest = CancelBookingRequest.newBuilder()
                    .setBookingId(request.bookingId())
                    .setReason(request.reason() != null ? request.reason() : "")
                    .build();

            bookingServiceStub.cancelBooking(grpcRequest);
            
            // Fetch updated booking
            return getBooking(request.bookingId());
        } catch (StatusRuntimeException e) {
            log.error("gRPC error cancelling booking: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to cancel booking: " + e.getStatus().getDescription());
        }
    }

    public BookingResponseDTO getBooking(String bookingId) {
        log.info("Gateway gRPC: Getting booking {}", bookingId);

        try {
            GetBookingRequest grpcRequest = GetBookingRequest.newBuilder()
                    .setBookingId(bookingId)
                    .build();

            BookingResponse grpcResponse = bookingServiceStub.getBooking(grpcRequest);
            return mapToBookingResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error getting booking: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to get booking: " + e.getStatus().getDescription());
        }
    }

    public ListBookingsResponseDTO listUserBookings(String userId) {
        log.info("Gateway gRPC: Listing bookings for user {}", userId);

        try {
            ListUserBookingsRequest grpcRequest = ListUserBookingsRequest.newBuilder()
                    .setUserId(userId)
                    .setPage(0)
                    .setSize(100)
                    .build();

            ListBookingsResponse grpcResponse = bookingServiceStub.listUserBookings(grpcRequest);
            
            List<BookingResponseDTO> bookings = grpcResponse.getBookingsList().stream()
                    .map(this::mapToBookingResponseDTO)
                    .collect(Collectors.toList());

            return new ListBookingsResponseDTO(bookings, grpcResponse.getTotalCount());
        } catch (StatusRuntimeException e) {
            log.error("gRPC error listing user bookings: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to list bookings: " + e.getStatus().getDescription());
        }
    }

    private BookingResponseDTO mapToBookingResponseDTO(BookingResponse response) {
        return new BookingResponseDTO(
                response.getBookingId(),
                response.getUserId(),
                response.getShowId(),
                null,
                Collections.emptyList(),
                response.getTotalAmount(),
                response.getStatus(),
                response.getEmail(),
                response.getPhone(),
                response.getLockId().isEmpty() ? null : response.getLockId(),
                response.getExpiresAt().isEmpty() ? null : response.getExpiresAt(),
                convertTimestampToLocalDateTime(response.getCreatedAt()),
                convertTimestampToLocalDateTime(response.getUpdatedAt())
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
