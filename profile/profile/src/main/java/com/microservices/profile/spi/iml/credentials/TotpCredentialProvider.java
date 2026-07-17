package com.microservices.profile.spi.iml.credentials;

import com.microservices.profile.encryption.EncryptionService;
import com.microservices.profile.exceptions.EncryptionException;
import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.spi.providers.CredentialProvider;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.stereotype.Service;

@Service
public class TotpCredentialProvider implements CredentialProvider {

    private final GoogleAuthenticator googleAuthenticator;
    private final EncryptionService encryptionService;

    public TotpCredentialProvider(GoogleAuthenticator googleAuthenticator, EncryptionService encryptionService) {
        this.googleAuthenticator = googleAuthenticator;
        this.encryptionService = encryptionService;
    }

    @Override
    public CredentialsType type() {
        return CredentialsType.TOTP;
    }

    @Override
    public Credential create(UserProfile userProfile, String secret) {
        Credential credential = new Credential();
        credential.setUserProfile(userProfile);
        credential.setType(type());
        credential.setSecret(encryptionService.encrypt(secret));
        credential.setStatus(CredentialStatus.PENDING);
        return credential;
    }

    @Override
    public boolean validate(Credential credential, String input) {
        return googleAuthenticator.authorize(encryptionService.decrypt( credential.getSecret()),
                Integer.parseInt(input));
    }

    @Override
    public Credential update(Credential credential, String secret) {

        credential.setSecret(encryptionService.encrypt(secret));
        return credential;
    }

}
