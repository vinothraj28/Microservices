package com.microservices.profile.models.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microservices.profile.models.audits.AuditableEntity;
import com.microservices.profile.models.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "user_profiles",
        indexes = {
                @Index(
                        name = "idx_user_email",
                        columnList = "email_address"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email_address"
                )
        }
)
public class UserProfile extends AuditableEntity implements BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, length = 15)
    private String providerID;

//    @Column(length = 60, nullable = false)
//    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Column(nullable = false)
    private Set<Roles> roles = new HashSet<>();

    @Override
    public UUID getId() {
        return this.userId;
    }
}
