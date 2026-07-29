package com.microservices.profile.services;

public interface PKCEValidator {
    void validateRequest(String codeChallenge, String codeChallengeMethod, boolean pkceRequired);
    boolean verifyCodeVerifier(String codeVerifier, String codeChallenge, String codeChallengeMethod);
}
