package com.microservices.profile.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Entity
@Table(name = "user_lookup")
@AllArgsConstructor
@NoArgsConstructor
public class UserLookup {

    @Id
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private String providerId;
}
