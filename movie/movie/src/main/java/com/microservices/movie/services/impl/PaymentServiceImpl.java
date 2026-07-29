package com.microservices.movie.services.impl;

import com.microservices.movie.configurations.MovieConfig;
import com.microservices.movie.exceptions.BookingException;
import com.microservices.movie.exceptions.PaymentException;
import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.models.entities.Booking;
import com.microservices.movie.models.entities.Payment;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.models.enums.PaymentMethod;
import com.microservices.movie.models.enums.PaymentStatus;
import com.microservices.movie.repositories.BookingRepository;
import com.microservices.movie.repositories.PaymentRepository;
import com.microservices.movie.services.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final MovieConfig movieConfig;

    @Override
    @Transactional
    public Payment initiatePayment(UUID bookingId, PaymentMethod paymentMethod) {
        log.info("Initiating payment for booking {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId.toString()));
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("Payment can only be initiated for pending bookings");
        }
        if (booking.isExpired()) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new PaymentException("Booking has expired");
        }

        return paymentRepository.findByBookingId(bookingId)
                .map(existingPayment -> {
                    log.debug("Returning existing payment {} for booking {}", existingPayment.getId(), bookingId);
                    return existingPayment;
                })
                .orElseGet(() -> paymentRepository.save(Payment.builder()
                        .booking(booking)
                        .amount(booking.getTotalAmount())
                        .paymentMethod(paymentMethod)
                        .status(PaymentStatus.INITIATED)
                        .transactionId(generateTransactionId("TXN"))
                        .message("Payment initiated")
                        .build()));
    }

    @Override
    @Transactional
    public Payment processPayment(UUID paymentId) {
        log.info("Processing payment {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId.toString()));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return payment;
        }
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new PaymentException("Refunded payments cannot be processed again");
        }

        payment.setStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);

        boolean success = ThreadLocalRandom.current().nextDouble() <= movieConfig.getPayment().getMockSuccessRate();
        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setMessage("Mock payment processed successfully");
            return paymentRepository.save(payment);
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setMessage("Mock payment failed");
        paymentRepository.save(payment);
        throw new PaymentException("Payment processing failed");
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentStatus verifyPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(Payment::getStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId.toString()));
    }

    @Override
    @Transactional
    public Payment processRefund(UUID bookingId, String refundReason) {
        log.info("Processing refund for booking {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId.toString()));
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", bookingId.toString()));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }
        if (payment.getStatus() != PaymentStatus.SUCCESS && payment.getStatus() != PaymentStatus.PARTIALLY_REFUNDED) {
            throw new PaymentException("Refund can only be processed for successful payments");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundAmount(payment.getAmount());
        payment.setRefundDate(LocalDateTime.now());
        payment.setRefundReason(refundReason);
        payment.setRefundTransactionId(generateTransactionId("RFND"));
        payment.setMessage("Refund processed for booking " + booking.getId());
        return paymentRepository.save(payment);
    }

    private String generateTransactionId(String prefix) {
        String transactionId;
        do {
            transactionId = prefix + "-" + UUID.randomUUID();
        } while (paymentRepository.existsByTransactionId(transactionId));
        return transactionId;
    }
}
