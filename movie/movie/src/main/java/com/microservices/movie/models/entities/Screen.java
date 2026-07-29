package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import com.microservices.movie.models.enums.ScreenType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "screens", 
    indexes = {
        @Index(name = "idx_screen_theater", columnList = "theater_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_theater_screen_number", columnNames = {"theater_id", "screen_number"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(nullable = false, length = 50)
    private String screenName;

    @Column(nullable = false)
    private Integer screenNumber;

    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScreenType screenType;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Show> shows = new ArrayList<>();

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setScreen(this);
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setScreen(null);
    }
}
