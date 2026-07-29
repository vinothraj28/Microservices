package com.microservices.gateway.services.sessions;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Manages the authenticated WebFlux session for the OAuth2 Authorization Code flow.
 *
 * The session stores only the information required to identify the authenticated
 * user and resume an interrupted authorization request.
 */
@Slf4j
@Service
public class OAuth2SessionService {

    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String RETURN_URL_ATTRIBUTE = "oauth_return_url";
    private static final String AUTHENTICATED_ATTRIBUTE = "authenticated";
    private static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);

    /**
     * Creates an authenticated session.
     */
    public Mono<Void> establishSession(WebSession session, String email) {
        log.info("Establishing session for {}", email);
        session.getAttributes().put(EMAIL_ATTRIBUTE, email);
        session.getAttributes().put(AUTHENTICATED_ATTRIBUTE, true);
        session.setMaxIdleTime(SESSION_TIMEOUT);
        return session.save();
    }

    /**
     * Returns true if the session belongs to an authenticated user.
     */
    public Mono<Boolean> isAuthenticated(WebSession session) {
        return Mono.just(hasEmail(session) && isAuthenticatedAttributeSet(session));
    }

    private boolean isAuthenticatedAttributeSet(WebSession session) {
        Boolean authenticated = session.getAttribute(AUTHENTICATED_ATTRIBUTE);
        return authenticated != null && authenticated;
    }

    /**
     * Invalidates the current session.
     */
    public Mono<Void> invalidateSession(WebSession session) {
        log.info("Invalidating session {}", session.getId());
        return session.invalidate();
    }

    /**
     * Saves the original OAuth2 authorize URL.
     */
    public Mono<Void> saveReturnUrl(WebSession session, String returnUrl) {
        session.getAttributes().put(RETURN_URL_ATTRIBUTE, returnUrl);
        return session.save();
    }
    /**
     * Returns the saved authorize URL.
     */
    public Mono<String> getReturnUrl(WebSession session) {
        String url = session.getAttribute(RETURN_URL_ATTRIBUTE);
        if (url == null || url.isBlank()) {
            return Mono.empty();
        }
        return Mono.just(url);
    }

    /**
     * Removes the saved authorize URL after it has been used.
     */
    public Mono<Void> clearReturnUrl(WebSession session) {
        session.getAttributes().remove(RETURN_URL_ATTRIBUTE);
        return session.save();
    }

    /**
     * Returns true if the session contains an authenticated email.
     */
    private boolean hasEmail(WebSession session) {
        String email = session.getAttribute(EMAIL_ATTRIBUTE);
        return email != null && !email.isBlank();
    }

    public Mono<Void> createPendingSession(WebSession session, @Email(message = "Email must be valid") @NotBlank(message = "Email cannot be blank") String email) {
        session.getAttributes().put(EMAIL_ATTRIBUTE, email);
        session.getAttributes().put(AUTHENTICATED_ATTRIBUTE, false);
        session.setMaxIdleTime(SESSION_TIMEOUT);
        return session.save().then(Mono.empty());
    }

    public Mono<String> getEmail(WebSession session) {
        String email = session.getAttribute(EMAIL_ATTRIBUTE);
        if (email == null || email.isBlank()) {
            return Mono.empty();
        }
        return Mono.just(email);
    }

    public Mono<Void> markAuthenticated(WebSession session) {
        session.getAttributes().put(AUTHENTICATED_ATTRIBUTE, true);
        return session.save().then(Mono.empty());
    }
}