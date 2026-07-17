package com.microservices.profile.spi.iml.credentials;

import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.spi.providers.CredentialProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * BCrypt password hashing and validation implementation.
 * 
 * Uses Spring Security's BCryptPasswordEncoder for secure password hashing
 * with automatic salt generation and configurable strength.
 * 
 * ⚠️ Thread-safe: BCryptPasswordEncoder is thread-safe and stateless.
 */
@Service
public class ByCryptCredentialProvider implements CredentialProvider {

    private final PasswordEncoder passwordEncoder;

    public ByCryptCredentialProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Returns the algorithm type identifier.
     * 
     * @return "BCRYPT" as a string
     */
    @Override
    public CredentialsType type() {
        return CredentialsType.PASSWORD;
    }

    @Override
    public Credential create(UserProfile userProfile, String secret) {
        String encodedPassword = passwordEncoder.encode(secret);
        Credential credential = new Credential();
        credential.setUserProfile(userProfile);
        credential.setSecret(encodedPassword);
        credential.setType(CredentialsType.PASSWORD);
        credential.setStatus(CredentialStatus.ACTIVE);
        return credential;
    }

    @Override
    public boolean validate(Credential credential, String input) {
        return passwordEncoder.matches(input, credential.getSecret());
    }

    @Override
    public Credential update(Credential credential, String newSecret) {
        String encodedPassword = passwordEncoder.encode(newSecret);
        credential.setSecret(encodedPassword);
        return credential;
    }



}
