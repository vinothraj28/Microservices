package com.microservices.profile.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservices.profile.models.audits.AuditableEntity;
import com.microservices.profile.models.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "addresses",
        indexes = {
                @Index(
                        name = "idx_address_user",
                        columnList = "user_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_address_type",
                        columnNames = {"user_id", "address_type"}
                )
        }
)
public class Address extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType;

    @Column(nullable = false, length = 50)
    private String zipcode;

    @Column(nullable = false, length = 255)
    private String street;

    @Column(nullable = false, length = 50)
    private String province;

    @Column(nullable = false, length = 50)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile userProfile;

}
