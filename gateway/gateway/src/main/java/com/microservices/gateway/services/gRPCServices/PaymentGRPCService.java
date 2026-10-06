package com.microservices.gateway.services.gRPCServices;

import com.google.protobuf.Timestamp;
import com.microservices.gateway.DTOS.payment.*;
import com.microservices.movie.grpc.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Gateway gRPC client service for Payment operations
 * Handles communication between Gateway REST API and Movie Service gRPC server
 * 
 * Design Pattern: Adapter Pattern - Converts DTOs to gRPC proto messages
 * Error Handling: Maps gRPC status codes to domain exceptions
 */
@Slf4j
@Service
public class PaymentGRPCService {

    @GrpcClient("movie-service")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceStub;

    public PaymentResponseDTO initiatePayment(InitiatePaymentRequestDTO request) {
        log.info("Gateway gRPC: Initiating payment for booking {} with method {}", 
                request.bookingId(), request.paymentMethod());

        try {
            InitiatePaymentRequest grpcRequest = InitiatePaymentRequest.newBuilder()
                    .setBookingId(request.bookingId())
                    .setAmount(request.amount())
                    .setPaymentMethod(request.paymentMethod())
                    .setUserId(request.userId())
                    .build();

            PaymentResponse grpcResponse = paymentServiceStub.initiatePayment(grpcRequest);
            return mapToPaymentResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error initiating payment: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to initiate payment: " + e.getStatus().getDescription());
        }
    }

    public PaymentResponseDTO verifyPayment(VerifyPaymentRequestDTO request) {
        log.info("Gateway gRPC: Verifying payment {} with transaction {}", 
                request.paymentId(), request.transactionId());

        try {
            VerifyPaymentRequest grpcRequest = VerifyPaymentRequest.newBuilder()
                    .setPaymentId(request.paymentId())
                    .setTransactionId(request.transactionId())
                    .build();

            PaymentResponse grpcResponse = paymentServiceStub.verifyPayment(grpcRequest);
            return mapToPaymentResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error verifying payment: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to verify payment: " + e.getStatus().getDescription());
        }
    }

    public RefundResponseDTO processRefund(String paymentId, Double refundAmount, String reason) {
        log.info("Gateway gRPC: Processing refund for payment {} with amount {}", paymentId, refundAmount);

        try {
            ProcessRefundRequest grpcRequest = ProcessRefundRequest.newBuilder()
                    .setPaymentId(paymentId)
                    .setRefundAmount(refundAmount)
                    .setReason(reason != null ? reason : "")
                    .build();

            RefundResponse grpcResponse = paymentServiceStub.processRefund(grpcRequest);
            return mapToRefundResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error processing refund: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to process refund: " + e.getStatus().getDescription());
        }
    }

    public PaymentResponseDTO getPaymentStatus(String paymentId) {
        log.info("Gateway gRPC: Getting payment status for {}", paymentId);

        try {
            GetPaymentStatusRequest grpcRequest = GetPaymentStatusRequest.newBuilder()
                    .setPaymentId(paymentId)
                    .build();

            PaymentResponse grpcResponse = paymentServiceStub.getPaymentStatus(grpcRequest);
            return mapToPaymentResponseDTO(grpcResponse);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error getting payment status: {}", e.getStatus(), e);
            throw new RuntimeException("Failed to get payment status: " + e.getStatus().getDescription());
        }
    }

    private PaymentResponseDTO mapToPaymentResponseDTO(PaymentResponse response) {
        return new PaymentResponseDTO(
                response.getPaymentId(),
                response.getBookingId(),
                response.getAmount(),
                response.getPaymentMethod(),
                response.getStatus(),
                response.getTransactionId().isEmpty() ? null : response.getTransactionId(),
                response.getMessage(),
                convertTimestampToLocalDateTime(response.getPaymentDate()),
                convertTimestampToLocalDateTime(response.getCreatedAt())
        );
    }

    private RefundResponseDTO mapToRefundResponseDTO(RefundResponse response) {
        return new RefundResponseDTO(
                response.getRefundId(),
                response.getPaymentId(),
                response.getRefundAmount(),
                response.getStatus(),
                response.getTransactionId(),
                response.getMessage(),
                convertTimestampToLocalDateTime(response.getRefundDate())
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
