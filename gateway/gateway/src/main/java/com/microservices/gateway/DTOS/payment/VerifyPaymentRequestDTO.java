package com.microservices.gateway.DTOS.payment;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for verifying a payment
 * 
 * @param paymentId UUID of the payment
 * @param transactionId External transaction ID
 */
public record VerifyPaymentRequestDTO(
        @NotBlank(message = "Payment ID is required")
        String paymentId,

        @NotBlank(message = "Transaction ID is required")
        String transactionId
) {
}
