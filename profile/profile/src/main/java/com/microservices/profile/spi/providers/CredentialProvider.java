package com.microservices.profile.spi.providers;


import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;

public interface CredentialProvider {

    CredentialsType type();
    Credential create(UserProfile userProfile, String secret);
    boolean validate(Credential credential, String input);
    Credential update(Credential credential, String secret);
}
