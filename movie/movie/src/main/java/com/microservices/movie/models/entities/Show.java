package com.microservices.movie.models.entities;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.models.audits.AuditableEntity;
import com.microservices.movie.models.enums.ShowType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shows", indexes = {
    @Index(name = "idx_show_movie", columnList = "movie_id"),
    @Index(name = "idx_show_screen", columnList = "screen_id"),
    @Index(name = "idx_show_datetime", columnList = "showDateTime"),
    @Index(name = "idx_show_theater_date", columnList = "theater_id, showDateTime")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(name = "theater_id", insertable = false, updatable = false)
    private UUID theaterId;

    @Column(nullable = false)
    private LocalDateTime showDateTime;

    @Column(nullable = false)
    private Double basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShowType showType;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void updateTheaterId() {
        if (screen != null && screen.getTheater() != null) {
            this.theaterId = screen.getTheater().getId();
        }
    }

    public int getAvailableSeatsCount() {
        if (screen == null) return 0;
        int totalSeats = screen.getTotalSeats();
        int bookedSeats = (int) bookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
            .count();
        return totalSeats - bookedSeats;
    }
}
