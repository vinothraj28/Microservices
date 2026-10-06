package com.microservices.movie.services.grpcServices;

import com.google.protobuf.Timestamp;
import com.microservices.movie.grpc.*;
import com.microservices.movie.models.entities.Payment;
import com.microservices.movie.models.enums.PaymentMethod;
import com.microservices.movie.models.enums.PaymentStatus;
import com.microservices.movie.services.interfaces.PaymentService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * gRPC service implementation for Payment operations
 * Handles InitiatePayment, VerifyPayment, ProcessRefund, and GetPaymentStatus RPC calls
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final PaymentService paymentService;

    @Override
    @Transactional
    public void initiatePayment(InitiatePaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        log.info("gRPC: Initiating payment for booking {} with method {}", 
                request.getBookingId(), request.getPaymentMethod());
        
        try {
            UUID bookingId = UUID.fromString(request.getBookingId());
            PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod());

            Payment payment = paymentService.initiatePayment(bookingId, paymentMethod);

            PaymentResponse response = mapPaymentToResponse(payment);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument in InitiatePayment request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid payment method or UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error initiating payment", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void verifyPayment(VerifyPaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        log.info("gRPC: Verifying payment {} with transaction {}", 
                request.getPaymentId(), request.getTransactionId());
        
        try {
            UUID paymentId = UUID.fromString(request.getPaymentId());
            PaymentStatus status = paymentService.verifyPayment(paymentId);

            PaymentResponse response = PaymentResponse.newBuilder()
                    .setPaymentId(paymentId.toString())
                    .setStatus(status.toString())
                    .setTransactionId(request.getTransactionId())
                    .setMessage("Payment verified")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in VerifyPayment request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error verifying payment", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional
    public void processRefund(ProcessRefundRequest request, StreamObserver<RefundResponse> responseObserver) {
        log.info("gRPC: Processing refund for payment {} with amount {}", 
                request.getPaymentId(), request.getRefundAmount());
        
        try {
            UUID paymentId = UUID.fromString(request.getPaymentId());
            
            RefundResponse response = RefundResponse.newBuilder()
                    .setRefundId(UUID.randomUUID().toString())
                    .setPaymentId(paymentId.toString())
                    .setRefundAmount(request.getRefundAmount())
                    .setStatus("INITIATED")
                    .setTransactionId("REFUND_" + UUID.randomUUID())
                    .setMessage("Refund initiated successfully")
                    .setRefundDate(convertToTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in ProcessRefund request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error processing refund", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaymentStatus(GetPaymentStatusRequest request, StreamObserver<PaymentResponse> responseObserver) {
        log.info("gRPC: Getting payment status for {}", request.getPaymentId());
        
        try {
            UUID paymentId = UUID.fromString(request.getPaymentId());
            PaymentStatus status = paymentService.verifyPayment(paymentId);

            PaymentResponse response = PaymentResponse.newBuilder()
                    .setPaymentId(paymentId.toString())
                    .setStatus(status.toString())
                    .setMessage("Payment status retrieved")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in GetPaymentStatus request", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format")
                    .asException());
        } catch (Exception e) {
            log.error("Error getting payment status", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asException());
        }
    }

    private PaymentResponse mapPaymentToResponse(Payment payment) {
        return PaymentResponse.newBuilder()
                .setPaymentId(payment.getId().toString())
                .setBookingId(payment.getBooking().getId().toString())
                .setAmount(payment.getAmount())
                .setPaymentMethod(payment.getPaymentMethod().toString())
                .setStatus(payment.getStatus().toString())
                .setTransactionId(payment.getTransactionId() != null ? payment.getTransactionId() : "")
                .setMessage(payment.getMessage() != null ? payment.getMessage() : "")
                .setPaymentDate(payment.getPaymentDate() != null ? convertToTimestamp(payment.getPaymentDate().toInstant(ZoneOffset.UTC)) : Timestamp.getDefaultInstance())
                .setCreatedAt(convertToTimestamp(payment.getCreatedAt()))
                .build();
    }

    private com.google.protobuf.Timestamp convertToTimestamp(Instant dateTime) {
        if (dateTime == null) {
            return com.google.protobuf.Timestamp.getDefaultInstance();
        }
        Instant instant = dateTime.atZone(ZoneOffset.UTC).toInstant();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
