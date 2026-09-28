package com.microservices.gateway.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieSecuritySettings {

    private final boolean secure;
    private final String sameSite;

    public CookieSecuritySettings(
            @Value("${app.cookies.secure:false}") boolean secure,
            @Value("${app.cookies.same-site:Strict}") String sameSite
    ) {
        this.secure = secure;
        this.sameSite = normalizeSameSite(sameSite);
        if ("None".equals(this.sameSite) && !this.secure) {
            throw new IllegalStateException("SameSite=None requires app.cookies.secure=true");
        }
        log.info("Cookie policy resolved: secure={}, sameSite={}, httpOnly=true, path=/", this.secure, this.sameSite);
    }

    public boolean secure() {
        return secure;
    }

    public String sameSite() {
        return sameSite;
    }

    public ResponseCookie.ResponseCookieBuilder applyDefaults(ResponseCookie.ResponseCookieBuilder builder) {
        return builder
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/");
    }

    private String normalizeSameSite(String value) {
        if (value == null || value.isBlank()) {
            return "Strict";
        }
        if ("strict".equalsIgnoreCase(value)) {
            return "Strict";
        }
        if ("lax".equalsIgnoreCase(value)) {
            return "Lax";
        }
        if ("none".equalsIgnoreCase(value)) {
            return "None";
        }
        throw new IllegalArgumentException("Invalid SameSite policy: " + value);
    }
}
