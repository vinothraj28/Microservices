package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import com.microservices.movie.models.enums.SeatStatus;
import com.microservices.movie.models.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "seats", 
    indexes = {
        @Index(name = "idx_seat_screen", columnList = "screen_id"),
        @Index(name = "idx_seat_row_number", columnList = "screen_id, rowName, seatNumber")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_screen_row_seat", columnNames = {"screen_id", "rowName", "seatNumber"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(nullable = false, length = 10)
    private String rowName;

    @Column(nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatType seatType;

    @Column(nullable = false)
    private Double priceMultiplier;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();
}
