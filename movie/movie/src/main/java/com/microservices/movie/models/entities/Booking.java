package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import com.microservices.movie.models.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_user", columnList = "userId"),
    @Index(name = "idx_booking_show", columnList = "show_id"),
    @Index(name = "idx_booking_status", columnList = "status"),
    @Index(name = "idx_booking_created", columnList = "createdAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column
    private UUID lockId;

    @Column
    private LocalDateTime expiresAt;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setBooking(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setBooking(null);
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
}
