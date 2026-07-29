package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.OAuth2ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class PKCEValidatorImplTest {

    private final PKCEValidatorImpl validator = new PKCEValidatorImpl();

    @Test
    @DisplayName("Should validate S256 PKCE request and verifier")
    void shouldValidateS256Pkce() throws Exception {
        String codeVerifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
        String codeChallenge = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(MessageDigest.getInstance("SHA-256")
                        .digest(codeVerifier.getBytes(StandardCharsets.US_ASCII)));

        validator.validateRequest(codeChallenge, "S256", true);
        assertTrue(validator.verifyCodeVerifier(codeVerifier, codeChallenge, "S256"));
    }

    @Test
    @DisplayName("Should reject malformed code verifier")
    void shouldRejectMalformedCodeVerifier() {
        assertThrows(
                OAuth2ValidationException.class,
                () -> validator.verifyCodeVerifier("short", "challenge", "plain")
        );
    }

    @Test
    @DisplayName("Should reject unsupported challenge method")
    void shouldRejectUnsupportedMethod() {
        assertThrows(
                OAuth2ValidationException.class,
                () -> validator.validateRequest("abc", "MD5", true)
        );
    }
}
