package com.microservices.gateway.DTOS.payment;

import java.time.LocalDateTime;

/**
 * DTO for payment response
 * Contains payment status and details
 * 
 * @param paymentId Unique payment identifier
 * @param bookingId ID of the associated booking
 * @param amount Payment amount
 * @param paymentMethod Payment method used
 * @param status Payment status (INITIATED, PROCESSING, SUCCESS, FAILED, REFUNDED)
 * @param transactionId External transaction ID
 * @param message Status message
 * @param paymentDate When payment was completed
 * @param createdAt Timestamp when payment was created
 */
public record PaymentResponseDTO(
        String paymentId,
        String bookingId,
        Double amount,
        String paymentMethod,
        String status,
        String transactionId,
        String message,
        LocalDateTime paymentDate,
        LocalDateTime createdAt
) {
}
