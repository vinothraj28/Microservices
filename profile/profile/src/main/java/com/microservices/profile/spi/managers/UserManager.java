package com.microservices.profile.spi.managers;

import com.microservices.profile.models.entities.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserManager {
    UserProfile createUser(UserProfile userProfile);
    UserProfile updateUser(UserProfile userProfile);
    Optional<UserProfile> getUserById(UUID userId);
    Optional<UserProfile> getUserByEmailAddress(String emailAddress);
    void deleteUser(UUID userId);
}
