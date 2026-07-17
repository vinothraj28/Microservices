package com.microservices.profile.services.Impl;

import com.microservices.profile.services.jwt.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.stereotype.Service;

@Service
public class ToptServiceIml implements TotpService {

    private final GoogleAuthenticator googleAuthenticator;

    public ToptServiceIml(GoogleAuthenticator googleAuthenticator) {
        this.googleAuthenticator = googleAuthenticator;
    }


    @Override
    public String generateSecret() {
        return googleAuthenticator.createCredentials().getKey();
    }

    @Override
    public String getUriForTotp(String secret, String email) {
        return "otpauth://totp/MyIAM:" + email + "?secret=" + secret + "&issuer=" + "MyIAM";
    }
}
