package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import com.microservices.movie.models.enums.PaymentMethod;
import com.microservices.movie.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_booking", columnList = "booking_id"),
    @Index(name = "idx_payment_transaction", columnList = "transactionId"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Column(unique = true, length = 100)
    private String transactionId;

    @Column(length = 500)
    private String message;

    @Column
    private LocalDateTime paymentDate;

    @Column
    private Double refundAmount;

    @Column(length = 100)
    private String refundTransactionId;

    @Column
    private LocalDateTime refundDate;

    @Column(length = 500)
    private String refundReason;
}
