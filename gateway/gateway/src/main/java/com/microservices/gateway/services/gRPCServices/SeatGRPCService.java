package com.microservices.gateway.services.gRPCServices;

import com.google.protobuf.Timestamp;
import com.microservices.gateway.DTOS.seat.*;
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
 * Gateway gRPC client service for Seat operations
 * Handles communication between Gateway REST API and Movie Service gRPC server
 * 
 * Design Pattern: Adapter Pattern - Converts DTOs to gRPC proto messages
 * Error Handling: Maps gRPC status codes to domain exceptions
 */
@Slf4j
@Service
public class SeatGRPCService {

    @GrpcClient("movie-service")
    private SeatServiceGrpc.SeatServiceBlockingStub seatServiceStub;

    public LockSeatsResponseDTO lockSeats(LockSeatsRequestDTO request) {
        log.info("Gateway gRPC: Locking {} seats for user {} on show {}", 
                request.seatIds().size(), request.userId(), request.showId());

        try {
            LockSeatsRequest grpcRequest = LockSeatsRequest.newBuilder()
                    .setShowId(request.showId())
                    .addAllSeatIds(request.seatIds())
                    .setUserId(request.userId())
                    .build();

            LockSeatsResponse grpcResponse = seatServiceStub.lockSeats(grpcRequest);
            
            return new LockSeatsResponseDTO(
                    grpcResponse.getSuccess(),
                    grpcResponse.getLockId(),
                    grpcResponse.getLockedUntil(),
                    grpcResponse.getLockedSeatIdsList(),
                    grpcResponse.getMessage()
            );
        } catch (StatusRuntimeException e) {
            log.error("gRPC error locking seats: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to lock seats: " + e.getStatus().getDescription());
        }
    }

    public Boolean unlockSeats(UnlockSeatsRequestDTO request) {
        log.info("Gateway gRPC: Unlocking seats with lock ID {}", request.lockId());

        try {
            UnlockSeatsRequest grpcRequest = UnlockSeatsRequest.newBuilder()
                    .setLockId(request.lockId())
                    .build();

            UnlockSeatsResponse grpcResponse = seatServiceStub.unlockSeats(grpcRequest);
            return grpcResponse.getSuccess();
        } catch (StatusRuntimeException e) {
            log.error("gRPC error unlocking seats: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to unlock seats: " + e.getStatus().getDescription());
        }
    }

    public SeatStatusResponseDTO getSeatStatus(GetSeatStatusRequestDTO request) {
        log.info("Gateway gRPC: Getting status for {} seats on show {}", 
                request.seatIds().size(), request.showId());

        try {
            GetSeatStatusRequest grpcRequest = GetSeatStatusRequest.newBuilder()
                    .setShowId(request.showId())
                    .addAllSeatIds(request.seatIds())
                    .build();

            GetSeatStatusResponse grpcResponse = seatServiceStub.getSeatStatus(grpcRequest);
            
            List<SeatStatusDTO> statuses = grpcResponse.getSeatStatusesList().stream()
                    .map(status -> new SeatStatusDTO(
                            status.getSeatId(),
                            status.getStatus(),
                            status.getLockedBy().isEmpty() ? null : status.getLockedBy(),
                            status.getLockedUntil().isEmpty() ? null : status.getLockedUntil()
                    ))
                    .collect(Collectors.toList());

            return new SeatStatusResponseDTO(statuses);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error getting seat status: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to get seat status: " + e.getStatus().getDescription());
        }
    }
}
