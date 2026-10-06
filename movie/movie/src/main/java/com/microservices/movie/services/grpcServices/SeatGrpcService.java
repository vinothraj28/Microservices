package com.microservices.movie.services.grpcServices;

import com.microservices.movie.grpc.*;
import com.microservices.movie.models.entities.SeatLock;
import com.microservices.movie.models.enums.SeatStatus;
import com.microservices.movie.services.interfaces.SeatLockService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for Seat operations
 * Handles LockSeats, UnlockSeats, and GetSeatStatus RPC calls
 * 
 * Time Complexity Analysis:
 * - lockSeats: O(n) where n is number of seats to lock
 * - unlockSeats: O(1)
 * - getSeatStatus: O(n) where n is number of seats to check
 * 
 * Space Complexity: O(n) for seat collections
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class SeatGrpcService extends SeatServiceGrpc.SeatServiceImplBase {

    private final SeatLockService seatLockService;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final int LOCK_TIMEOUT_MINUTES = 5;

    @Override
    @Transactional
    public void lockSeats(LockSeatsRequest request, StreamObserver<LockSeatsResponse> responseObserver) {
        log.info("gRPC: Locking {} seats for user {} on show {}", 
                request.getSeatIdsList().size(), request.getUserId(), request.getShowId());
        
        try {
            UUID showId = UUID.fromString(request.getShowId());
            UUID userId = UUID.fromString(request.getUserId());
            List<UUID> seatIds = request.getSeatIdsList().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());

            SeatLock seatLock = seatLockService.lockSeats(showId, userId, seatIds);

            LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(LOCK_TIMEOUT_MINUTES);
            
            LockSeatsResponse response = LockSeatsResponse.newBuilder()
                    .setSuccess(true)
                    .setLockId(seatLock.getId().toString())
                    .setLockedUntil(lockedUntil.format(ISO_FORMATTER))
                    .addAllLockedSeatIds(request.getSeatIdsList())
                    .setMessage("Seats locked successfully for 5 minutes")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.info("gRPC: Seats locked successfully with lock ID {}", seatLock.getId());
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in LockSeats request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error locking seats", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void unlockSeats(UnlockSeatsRequest request, StreamObserver<UnlockSeatsResponse> responseObserver) {
        log.info("gRPC: Unlocking seats with lock ID {}", request.getLockId());
        
        try {
            UUID lockId = UUID.fromString(request.getLockId());
            
            // Service call would unlock seats (implementation depends on SeatLockService)
            seatLockService.unlockSeats(lockId, null);

            UnlockSeatsResponse response = UnlockSeatsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Seats unlocked successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.info("gRPC: Seats unlocked successfully for lock {}", lockId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in UnlockSeats request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error unlocking seats", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void getSeatStatus(GetSeatStatusRequest request, StreamObserver<GetSeatStatusResponse> responseObserver) {
        log.info("gRPC: Getting status for {} seats on show {}", 
                request.getSeatIdsList().size(), request.getShowId());
        
        try {
            UUID showId = UUID.fromString(request.getShowId());
            
//            List<SeatStatus> seatStatuses = request.getSeatIdsList().stream()
//                    .map(seatId -> {
//                        try {
//                            return seatLockService.getSeatStatus(showId, UUID.fromString(seatId));
//                        } catch (Exception e) {
//                            log.warn("Error getting status for seat {}: {}", seatId, e.getMessage());
//                            return null;
//                        }
//                    })
//                    .filter(status -> status != null)
//                    .collect(Collectors.toList());

            List<com.microservices.movie.grpc.SeatStatus> protoStatuses = request.getSeatIdsList().stream()
                    .map(seatId -> com.microservices.movie.grpc.SeatStatus.newBuilder()
                            .setSeatId( seatId != null ? seatId : "")
                            .setStatus(seatLockService.getSeatStatus(showId, UUID.fromString(seatId)).toString())
                            .build())
                    .collect(Collectors.toList());

            GetSeatStatusResponse response = GetSeatStatusResponse.newBuilder()
                    .addAllSeatStatuses(protoStatuses)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.info("gRPC: Seat status retrieved successfully for {} seats", protoStatuses.size());
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in GetSeatStatus request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error getting seat status", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }
}
