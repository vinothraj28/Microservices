package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "theaters", indexes = {
    @Index(name = "idx_theater_city", columnList = "city"),
    @Index(name = "idx_theater_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @ElementCollection
    @CollectionTable(name = "theater_amenities", joinColumns = @JoinColumn(name = "theater_id"))
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Screen> screens = new ArrayList<>();

    public void addScreen(Screen screen) {
        screens.add(screen);
        screen.setTheater(this);
    }

    public void removeScreen(Screen screen) {
        screens.remove(screen);
        screen.setTheater(null);
    }
}
