package com.microservices.profile.spi.iml.credentials;

import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.repository.CredentialRepository;
import com.microservices.profile.spi.managers.CredentialsManager;
import com.microservices.profile.spi.providers.CredentialProvider;
import com.microservices.profile.spi.registry.CredentialProviderRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultCredentialManager implements CredentialsManager {

    private final CredentialProviderRegistry registry;
    private final CredentialRepository credentialRepository;

    public DefaultCredentialManager(CredentialProviderRegistry registry,
                                    CredentialRepository credentialRepository) {
        this.registry = registry;
        this.credentialRepository = credentialRepository;
    }

    @Override
    public Credential createCredential(UserProfile user, CredentialsType type, String secret) {
        CredentialProvider provider = registry.get(type);
        Credential credential = provider.create(user, secret);
        Credential saved = credentialRepository.save(credential);
        return saved;
    }

    @Override
    public boolean validateCredential(UserProfile user, CredentialsType type, String secret) {
        CredentialProvider provider = registry.get(type);
        Credential credential = credentialRepository.findByUserProfileIdAndType(
                user.getId(),
                provider.type()
        ).orElseThrow(() -> new IllegalArgumentException("Credential not found for user: " + user.getId()));
        return provider.validate(credential, secret);
    }

    @Override
    public Credential updateCredential(UserProfile user, CredentialsType type, String newSecret) {
        CredentialProvider provider = registry.get(type);
        Credential credential = credentialRepository.findByUserProfileIdAndType(
                user.getId(),
                provider.type()
        ).orElseThrow(() -> new IllegalArgumentException("Credential not found for user: "  + user.getId()));
        provider.update(credential, newSecret);
        Credential saved = credentialRepository.save(credential);
        return saved;
    }

    @Override
    public Credential updateCredentialStatus(UserProfile user, CredentialsType type, CredentialStatus status) {
        CredentialProvider provider = registry.get(type);
        Credential credential = credentialRepository.findByUserProfileIdAndType(
                user.getId(),
                provider.type()
        ).orElseThrow(() -> new IllegalArgumentException("Credential not found for user: "  + user.getId()));
        credential.setStatus(status);
        Credential saved = credentialRepository.save(credential);
        return saved;
    }

    @Override
    public Optional<Credential> getActiveCredential(UserProfile user, CredentialsType type) {
        CredentialProvider provider = registry.get(type);
        Optional<Credential> credential = credentialRepository.findByUserProfileIdAndType(
                user.getId(),
                provider.type()
        );

        // Filter to only return credentials with ACTIVE status
        return credential.filter(c -> c.getStatus() == CredentialStatus.ACTIVE);
    }

}
