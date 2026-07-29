package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import com.microservices.movie.models.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets", 
    indexes = {
        @Index(name = "idx_ticket_booking", columnList = "booking_id"),
        @Index(name = "idx_ticket_show", columnList = "show_id"),
        @Index(name = "idx_ticket_seat", columnList = "seat_id"),
        @Index(name = "idx_ticket_qr", columnList = "qrCode")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_ticket_qr", columnNames = {"qrCode"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(nullable = false, unique = true, length = 500)
    private String qrCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Column(nullable = false)
    private Double price;

    @Column
    private LocalDateTime issuedAt;

    @Column
    private LocalDateTime usedAt;

    public boolean isValid() {
        return status == TicketStatus.ISSUED && 
               show != null && 
               show.getShowDateTime().isAfter(LocalDateTime.now());
    }
}
