package com.microservices.gateway.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.server.session.WebSessionIdResolver;
import org.springframework.web.server.session.WebSessionStore;

import java.time.Duration;

/**
 * Configuration for in-memory session management.
 * Uses InMemoryWebSessionStore for local development.
 * For production with multiple instances, use Redis via @EnableRedisWebSession.
 */
@Slf4j
@Configuration
public class SessionConfig {

    private final CookieSecuritySettings cookieSecuritySettings;

    public SessionConfig(CookieSecuritySettings cookieSecuritySettings) {
        this.cookieSecuritySettings = cookieSecuritySettings;
    }

    /**
     * In-memory session store for development.
     * Sessions are stored in application memory (not shared across instances).
     */
    @Bean
    public WebSessionStore webSessionStore() {
        InMemoryWebSessionStore store = new InMemoryWebSessionStore();
        store.setMaxSessions(1000); // Limit to prevent memory issues
        return store;
    }

    /**
     * Configures session cookie settings.
     */
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("IAM_SESSION");
        log.info("Configuring IAM_SESSION cookie policy: secure={}, sameSite={}, httpOnly=true, path=/, maxAgeMinutes=30",
                cookieSecuritySettings.secure(), cookieSecuritySettings.sameSite());
        resolver.addCookieInitializer(builder -> cookieSecuritySettings
                .applyDefaults(builder)
                .maxAge(Duration.ofMinutes(30))
        );
        return resolver;
    }
}