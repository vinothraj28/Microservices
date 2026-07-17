package com.microservices.profile.repository;

import com.microservices.profile.models.entities.UserLookup;
import com.microservices.profile.models.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserLookUpRepo extends JpaRepository<UserLookup, UUID> {

    @Query("SELECT u.providerId FROM UserLookup u WHERE u.userId = :userId")
    Optional<String> findProviderByUserId(UUID userId);



    @Query("SELECT u.providerId FROM UserLookup u WHERE u.emailAddress = :emailAddress")
    Optional<String> findProviderByUserEmail(String emailAddress);

}
