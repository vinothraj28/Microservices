package com.microservices.movie.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String fileName;

    private String contentType;

    private Long size;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data; // temporary if storing in DB

    private String storageKey; // used after moving to cloud storage

    private String provider;

}
