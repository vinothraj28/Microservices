package com.microservices.gateway.DTOS.payment;

import java.time.LocalDateTime;

/**
 * DTO for refund response
 * Contains refund information
 * 
 * @param refundId Unique refund identifier
 * @param paymentId ID of the refunded payment
 * @param refundAmount Amount refunded
 * @param status Refund status (INITIATED, PROCESSING, SUCCESS, FAILED)
 * @param transactionId Refund transaction ID
 * @param message Status message
 * @param refundDate When refund was processed
 */
public record RefundResponseDTO(
        String refundId,
        String paymentId,
        Double refundAmount,
        String status,
        String transactionId,
        String message,
        LocalDateTime refundDate
) {
}
