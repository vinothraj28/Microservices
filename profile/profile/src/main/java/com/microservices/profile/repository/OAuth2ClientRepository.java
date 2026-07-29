package com.microservices.profile.repository;

import com.microservices.profile.models.entities.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, UUID> {
    Optional<OAuth2Client> findByClientId(String clientId);
    Optional<OAuth2Client> findByClientIdAndIsActiveTrue(String clientId);
    boolean existsByClientId(String clientId);
}
