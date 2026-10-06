package com.microservices.movie.services.grpcServices;

import com.microservices.movie.grpc.*;
import com.microservices.movie.mappers.MovieMapper;
import com.microservices.movie.mappers.ScreenMapper;
import com.microservices.movie.mappers.ShowMapper;
import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.services.interfaces.BookingService;
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
 * gRPC service implementation for Booking operations
 * Handles CreateBooking, ConfirmBooking, CancelBooking, GetBooking, and ListUserBookings RPC calls
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class BookingGrpcService extends BookingServiceGrpc.BookingServiceImplBase {

    private final BookingService bookingService;
    private final ShowMapper showMapper;
    private final ScreenMapper screenMapper;
    private final MovieMapper movieMapper;

    @Override
    @Transactional
    public void createBooking(CreateBookingRequest request, StreamObserver<BookingResponse> responseObserver) {
        log.info("gRPC: Creating booking for user {} on show {}", request.getUserId(), request.getShowId());
        
        try {
            UUID showId = UUID.fromString(request.getShowId());
            UUID userId = UUID.fromString(request.getUserId());
            UUID lockId = UUID.fromString(request.getLockId());

            Booking booking = bookingService.createBooking(showId, userId, lockId, request.getEmail(), request.getPhone());

            BookingResponse response = mapBookingToResponse(booking);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in CreateBooking request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error creating booking", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void confirmBooking(ConfirmBookingRequest request, StreamObserver<BookingResponse> responseObserver) {
        log.info("gRPC: Confirming booking {} with payment {}", request.getBookingId(), request.getPaymentId());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());
            UUID paymentId = UUID.fromString(request.getPaymentId());

            Booking booking = bookingService.confirmBooking(bookingId, paymentId);

            BookingResponse response = mapBookingToResponse(booking);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in ConfirmBooking request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error confirming booking", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void cancelBooking(CancelBookingRequest request, StreamObserver<CancelBookingResponse> responseObserver) {
        log.info("gRPC: Cancelling booking {} with reason: {}", request.getBookingId(), request.getReason());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());

            Booking booking = bookingService.cancelBooking(bookingId, request.getReason());

            CancelBookingResponse response = CancelBookingResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Booking cancelled successfully")
                    .setRefundId(UUID.randomUUID().toString())
                    .setRefundAmount(booking.getTotalAmount())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in CancelBooking request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error cancelling booking", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void getBooking(GetBookingRequest request, StreamObserver<BookingResponse> responseObserver) {
        log.info("gRPC: Getting booking {}", request.getBookingId());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());
            Booking booking = bookingService.getBooking(bookingId);

            BookingResponse response = mapBookingToResponse(booking);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in GetBooking request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error getting booking", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void listUserBookings(ListUserBookingsRequest request, StreamObserver<ListBookingsResponse> responseObserver) {
        log.info("gRPC: Listing bookings for user {}", request.getUserId());
        
        try {
            UUID userId = UUID.fromString(request.getUserId());
            List<Booking> bookings = bookingService.listUserBookings(userId);

            List<BookingResponse> bookingResponses = bookings.stream()
                    .map(this::mapBookingToResponse)
                    .collect(Collectors.toList());

            ListBookingsResponse response = ListBookingsResponse.newBuilder()
                    .addAllBookings(bookingResponses)
                    .setTotalCount(bookingResponses.size())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in ListUserBookings request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error listing user bookings", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    private BookingResponse mapBookingToResponse(Booking booking) {
        return BookingResponse.newBuilder()
                .setBookingId(booking.getId().toString())
                .setUserId(booking.getUserId().toString())
                .setShowId(booking.getShow().getId().toString())
                .setShow(showMapper.toShowResponse(booking.getShow(), movieMapper, screenMapper))
                .setTotalAmount(booking.getTotalAmount())
                .setStatus(booking.getStatus().toString())
                .setEmail(booking.getEmail())
                .setPhone(booking.getPhone())
                .setLockId(booking.getLockId() != null ? booking.getLockId().toString() : "")
                .setExpiresAt(booking.getExpiresAt() != null ? booking.getExpiresAt().toString() : "")
                .setCreatedAt(convertToTimestamp(booking.getCreatedAt()))
                .setUpdatedAt( convertToTimestamp(booking.getUpdatedAt()))
                .build();
    }

    private com.google.protobuf.Timestamp convertToTimestamp(Instant dateTime) {
        if (dateTime == null) {
            return com.google.protobuf.Timestamp.getDefaultInstance();
        }
        Instant instant = dateTime.atZone(ZoneOffset.UTC).toInstant();
        return com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
