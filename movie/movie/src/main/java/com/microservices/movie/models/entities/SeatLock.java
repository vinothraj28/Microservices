package com.microservices.movie.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "seat_locks", 
    indexes = {
        @Index(name = "idx_lock_show_user", columnList = "show_id, user_id"),
        @Index(name = "idx_lock_expiry", columnList = "lockExpiry"),
        @Index(name = "idx_lock_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatLock implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID showId;

    @Column(nullable = false)
    private UUID userId;

    @ElementCollection
    @CollectionTable(name = "seat_lock_seats", joinColumns = @JoinColumn(name = "lock_id"))
    @Column(name = "seat_id")
    private List<UUID> seatIds = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime lockExpiry;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, EXPIRED, RELEASED, CONVERTED

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(lockExpiry);
    }

    public boolean isActive() {
        return "ACTIVE".equals(status) && !isExpired();
    }
}
