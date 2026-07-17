package com.microservices.profile.spi.iml.users;

import com.microservices.profile.spi.providers.UserProvider;
import com.microservices.profile.spi.registry.UserProviderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DefaultUserProviderRegistry implements UserProviderRegistry {

    private final Map<String, UserProvider> registry;

    public DefaultUserProviderRegistry(List<UserProvider> providers) {
        this.registry = providers.stream().collect(Collectors.toUnmodifiableMap(
                UserProvider::getProviderId,
                Function.identity()
        ));
    }

    @Override
    public Optional<UserProvider> get(String providerId) {
        return Optional.ofNullable(registry.get(providerId));
    }
}
