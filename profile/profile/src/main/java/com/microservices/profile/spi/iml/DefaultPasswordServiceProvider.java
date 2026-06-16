package com.microservices.profile.spi.iml;

import com.microservices.profile.services.PasswordService;
import com.microservices.profile.spi.PasswordServiceProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class DefaultPasswordServiceProvider implements PasswordServiceProvider {

    private final Map<String, PasswordService> registry;

    public DefaultPasswordServiceProvider(List<PasswordService> services){
        this.registry = services.stream()
                .collect(Collectors.toUnmodifiableMap(
                PasswordService::type,
                Function.identity()
        ));;
    }

    @Override
    public PasswordService get(String type) {
        return Optional.ofNullable(registry.get(type))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown password service: " + type));
    }
}
