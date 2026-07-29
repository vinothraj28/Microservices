package com.microservices.movie.models.entities;

import com.microservices.movie.models.audits.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "movies", indexes = {
    @Index(name = "idx_movie_genre", columnList = "genre"),
    @Index(name = "idx_movie_release_date", columnList = "releaseDate"),
    @Index(name = "idx_movie_language", columnList = "language")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(nullable = false, length = 50)
    private String language;

    @Column(nullable = false)
    private LocalDate releaseDate;

    //marked for deletion
    @Column(length = 500)
    private String posterUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(length = 500)
    private String trailerUrl;

    @Column(nullable = false, length = 10)
    private String rating; // U, UA, A, R

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "cast_member")
    private List<String> cast = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_crew", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "crew_member")
    private List<String> crew = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Show> shows = new ArrayList<>();
}
