package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Payment;
import com.microservices.movie.models.enums.PaymentMethod;
import com.microservices.movie.models.enums.PaymentStatus;

import java.util.UUID;

public interface PaymentService {

    Payment initiatePayment(UUID bookingId, PaymentMethod paymentMethod);

    Payment processPayment(UUID paymentId);

    PaymentStatus verifyPayment(UUID paymentId);

    Payment processRefund(UUID bookingId, String refundReason);
}
