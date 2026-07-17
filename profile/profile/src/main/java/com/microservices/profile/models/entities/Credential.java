package com.microservices.profile.models.entities;

import com.microservices.profile.models.audits.AuditableEntity;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "credentials",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_credential_type",
                        columnNames = {"user_id", "type"}
                )
        }
)
public class Credential extends AuditableEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID credentialId;

    @Column(nullable = false, length = 255)
    private String secret;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CredentialsType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CredentialStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile userProfile;

    @Override
    public UUID getId() {
        return this.credentialId;
    }
}
