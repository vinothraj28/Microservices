package com.microservices.profile.spi.registry;

import com.microservices.profile.spi.providers.UserProvider;

import java.util.Optional;

public interface UserProviderRegistry {
    Optional<UserProvider> get(String providerId);
}
