package com.microservices.profile.spi.managers;

import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.spi.providers.CredentialProvider;

import java.util.Optional;

public interface CredentialsManager {

    Credential createCredential(
            UserProfile user,
            CredentialsType type,
            String secret
    );

    boolean validateCredential(
            UserProfile user,
            CredentialsType type,
            String secret
    );

    Credential updateCredential(
            UserProfile user,
            CredentialsType type,
            String newSecret
    );

    Credential updateCredentialStatus(
            UserProfile user,
            CredentialsType type,
            CredentialStatus status
    );

    /**
     * Retrieves an active credential of a specific type for a user.
     *
     * Returns an Optional that is:
     * - Empty if no credential of this type exists
     * - Empty if the credential exists but is not ACTIVE
     * - Present if an ACTIVE credential of this type is found
     *
     * @param user User to lookup credential for
     * @param type Type of credential (e.g., TOTP for MFA)
     * @return Optional containing the active credential, or empty if not found/not active
     */
    Optional<Credential> getActiveCredential(UserProfile user, CredentialsType type);

}
