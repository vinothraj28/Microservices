package com.microservices.profile.spi.iml.users;

import com.microservices.profile.exceptions.DuplicateEmailException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.repository.UserProfileRepository;
import com.microservices.profile.spi.providers.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class JPAUserProvider implements UserProvider {

    private final UserProfileRepository userProfileRepository;

    public JPAUserProvider(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public String getProviderId() {
        return "JPAUserProvider";
    }

    @Override
    public UserProfile createUser(UserProfile userProfile) {
        log.info("Creating user with email inside JPAProvider: {}", userProfile.getEmailAddress());
        if(userProfileRepository.findByEmailAddress(userProfile.getEmailAddress()).isPresent()){
            throw new IllegalArgumentException("User with email " + userProfile.getEmailAddress() + " already exists.");
        }
        userProfile.setProviderID(getProviderId());
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile updateUser(UserProfile userProfile) {
        UserProfile existing =
                userProfileRepository.findById(userProfile.getId())
                        .orElseThrow(() ->
                                new UserNotFoundException("User not found")
                        );

        if (!existing.getEmailAddress()
                .equals(userProfile.getEmailAddress())
                &&
                userProfileRepository.findByEmailAddress(
                        userProfile.getEmailAddress()
                ).isPresent()) {
            throw new DuplicateEmailException("Email already in use");
        }
        existing.setDob(userProfile.getDob());
        existing.setEmailAddress(userProfile.getEmailAddress());
        existing.setProviderID(userProfile.getProviderID());
        existing.setUserName(userProfile.getUserName());
        return userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> getUserById(UUID userId) {
        return userProfileRepository.findById(userId);
    }

    @Override
    public Optional<UserProfile> getUserByEmailAddress(String emailAddress) {
        return userProfileRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public void deleteUser(UUID userId) {
        userProfileRepository.deleteById(userId);
    }
}
