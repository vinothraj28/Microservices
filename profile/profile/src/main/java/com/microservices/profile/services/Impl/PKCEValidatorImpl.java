package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.OAuth2ValidationException;
import com.microservices.profile.services.PKCEValidator;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;

@Service
public class PKCEValidatorImpl implements PKCEValidator {

    private static final Pattern CODE_VERIFIER_PATTERN = Pattern.compile("^[A-Za-z0-9\\-._~]{43,128}$");
    private static final String METHOD_S256 = "S256";
    private static final String METHOD_PLAIN = "plain";

    @Override
    public void validateRequest(String codeChallenge, String codeChallengeMethod, boolean pkceRequired) {
        if (!pkceRequired && isBlank(codeChallenge) && isBlank(codeChallengeMethod)) {
            return;
        }

        if (isBlank(codeChallenge) || isBlank(codeChallengeMethod)) {
            throw new OAuth2ValidationException("PKCE code_challenge and code_challenge_method are required");
        }

        if (!METHOD_S256.equals(codeChallengeMethod) && !METHOD_PLAIN.equals(codeChallengeMethod)) {
            throw new OAuth2ValidationException("Unsupported code_challenge_method. Use S256 or plain");
        }

        if (codeChallenge.length() < 43 || codeChallenge.length() > 128) {
            throw new OAuth2ValidationException("Invalid code_challenge length");
        }
    }

    @Override
    public boolean verifyCodeVerifier(String codeVerifier, String codeChallenge, String codeChallengeMethod) {
        if (isBlank(codeVerifier)) {
            throw new OAuth2ValidationException("code_verifier is required");
        }
        if (!CODE_VERIFIER_PATTERN.matcher(codeVerifier).matches()) {
            throw new OAuth2ValidationException("Invalid code_verifier format");
        }
        if (isBlank(codeChallenge) || isBlank(codeChallengeMethod)) {
            throw new OAuth2ValidationException("Authorization code is missing PKCE metadata");
        }

        String expectedChallenge = METHOD_S256.equals(codeChallengeMethod)
                ? generateS256Challenge(codeVerifier)
                : codeVerifier;

        return MessageDigest.isEqual(
                expectedChallenge.getBytes(StandardCharsets.UTF_8),
                codeChallenge.getBytes(StandardCharsets.UTF_8)
        );
    }

    private String generateS256Challenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
        } catch (NoSuchAlgorithmException ex) {
            throw new OAuth2ValidationException("Unable to validate PKCE challenge", ex);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
