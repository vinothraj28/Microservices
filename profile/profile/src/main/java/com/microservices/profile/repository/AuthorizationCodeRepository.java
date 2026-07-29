package com.microservices.profile.repository;

import com.microservices.profile.models.entities.AuthorizationCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, UUID> {
    Optional<AuthorizationCode> findByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ac FROM AuthorizationCode ac WHERE ac.code = :code")
    Optional<AuthorizationCode> findLockedByCode(@Param("code") String code);

    long deleteByExpiresAtBefore(LocalDateTime now);
}
