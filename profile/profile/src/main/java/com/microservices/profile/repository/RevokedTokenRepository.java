package com.microservices.profile.repository;

import com.microservices.profile.models.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, UUID> {
    boolean existsByJtiAndExpiresAtAfter(String jti, LocalDateTime now);
    Optional<RevokedToken> findByJti(String jti);
    long deleteByExpiresAtBefore(LocalDateTime now);
}
