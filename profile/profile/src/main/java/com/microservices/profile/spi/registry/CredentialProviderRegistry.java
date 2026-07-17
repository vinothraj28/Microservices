package com.microservices.profile.spi.registry;

import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.spi.providers.CredentialProvider;

public interface CredentialProviderRegistry {
    CredentialProvider get(CredentialsType type);
}
