package com.microservices.profile.repository;

import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.enums.CredentialsType;
import jdk.dynalink.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {

     Optional<Credential> findByUserProfileIdAndType(
            UUID userProfileId,
            CredentialsType type
    );
}
