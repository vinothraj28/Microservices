package com.microservices.gateway.configurations;

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
@Configuration
public class SessionConfig {

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
        resolver.addCookieInitializer(builder -> builder
                .path("/")
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .sameSite("Lax") // Allow cross-origin for OAuth2 redirects
                .maxAge(Duration.ofMinutes(30))
        );
        return resolver;
    }
}