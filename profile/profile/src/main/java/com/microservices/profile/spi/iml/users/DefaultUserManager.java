package com.microservices.profile.spi.iml.users;

import com.microservices.profile.exceptions.ProviderNotFoundException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.models.entities.UserLookup;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.repository.UserLookUpRepo;
import com.microservices.profile.spi.managers.UserManager;
import com.microservices.profile.spi.providers.UserProvider;
import com.microservices.profile.spi.registry.UserProviderRegistry;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class DefaultUserManager implements UserManager {

    private final UserProviderRegistry userProviderRegistry;
    private final UserLookUpRepo userLookUpRepo;

    public DefaultUserManager(UserProviderRegistry userProviderRegistry, UserLookUpRepo userLookUpRepo) {
        this.userProviderRegistry = userProviderRegistry;
        this.userLookUpRepo = userLookUpRepo;
    }

    @Override
    public UserProfile createUser(UserProfile userProfile) {
        //use default provider to create user and save the lookup
        UserProfile saved = userProviderRegistry.get("JPAUserProvider")
                .orElseThrow(()->new ProviderNotFoundException("Provider not found")).createUser(userProfile);
        userLookUpRepo.save(new UserLookup(saved.getUserId(),saved.getEmailAddress(), saved.getProviderID()));
        return saved;
    }

    @Override
    public UserProfile updateUser(UserProfile userProfile) {
        return getUserProvider(userProfile.getUserId()).updateUser(userProfile);
    }

    @Override
    public Optional<UserProfile> getUserById(UUID userId) {
        return getUserProvider(userId).getUserById(userId);
    }

    @Override
    public Optional<UserProfile> getUserByEmailAddress(String emailAddress) {
        return getUserProvider(emailAddress).getUserByEmailAddress(emailAddress);
    }

    @Override
    public void deleteUser(UUID userId) {
        getUserProvider(userId).deleteUser(userId);
    }

    public UserProvider getUserProvider(UUID userId) {
        String providerId = userLookUpRepo.findProviderByUserId(userId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return userProviderRegistry.get(providerId).orElseThrow(() ->
                new ProviderNotFoundException(providerId)
        );
    }

    public UserProvider getUserProvider(String emailAddress) {
        String providerId = userLookUpRepo.findProviderByUserEmail(emailAddress).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return userProviderRegistry.get(providerId).orElseThrow(() ->
                new ProviderNotFoundException(providerId)
        );
    }
}

