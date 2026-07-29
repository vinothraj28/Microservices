package com.microservices.movie.repositories;

import com.microservices.movie.models.entities.Payment;
import com.microservices.movie.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByBookingId(UUID bookingId);
    Optional<Payment> findByTransactionId(String transactionId);
    boolean existsByTransactionId(String transactionId);
}
