package com.microservices.profile.services.jwt;

public interface TotpService {

    String generateSecret();
    String getUriForTotp(String secret, String email);

}
