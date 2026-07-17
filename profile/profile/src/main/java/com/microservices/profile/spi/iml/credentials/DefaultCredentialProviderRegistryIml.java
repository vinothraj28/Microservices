package com.microservices.profile.spi.iml.credentials;

import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.spi.providers.CredentialProvider;
import com.microservices.profile.spi.registry.CredentialProviderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DefaultCredentialProviderRegistryIml implements CredentialProviderRegistry {

    private final Map<CredentialsType, CredentialProvider> registry;

    public DefaultCredentialProviderRegistryIml(List<CredentialProvider> services){
        this.registry = services.stream()
                .collect(Collectors.toUnmodifiableMap(
                CredentialProvider::type,
                Function.identity()
        ));;
    }

    @Override
    public CredentialProvider get(CredentialsType type) {
        return Optional.ofNullable(registry.get(type))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown password service: " + type));
    }
}
