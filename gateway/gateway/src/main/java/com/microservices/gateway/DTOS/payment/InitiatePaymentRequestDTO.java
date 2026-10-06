package com.microservices.gateway.DTOS.payment;

import jakarta.validation.constraints.*;

/**
 * DTO for initiating a payment
 * 
 * @param bookingId UUID of the booking
 * @param amount Amount to be paid
 * @param paymentMethod Payment method (CREDIT_CARD, DEBIT_CARD, UPI, WALLET, NET_BANKING)
 * @param userId UUID of the user
 */
public record InitiatePaymentRequestDTO(
        @NotBlank(message = "Booking ID is required")
        String bookingId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        Double amount,

        @NotBlank(message = "Payment method is required")
        @Pattern(regexp = "CREDIT_CARD|DEBIT_CARD|UPI|WALLET|NET_BANKING", 
                 message = "Invalid payment method")
        String paymentMethod,

        @NotBlank(message = "User ID is required")
        String userId
) {
}
