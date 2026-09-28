package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.oauth2.OAuth2AuthorizeResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2ClientRegistrationRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2ClientRegistrationResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2IntrospectRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2IntrospectResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2RevokeRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2RevokeResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2TokenRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2TokenResponseDTO;
import com.microservices.gateway.DTOS.sessions.SessionEstablishRequestDTO;
import com.microservices.gateway.DTOS.sessions.SessionEstablishResponseDTO;
import com.microservices.gateway.configurations.CookieSecuritySettings;
import com.microservices.gateway.excpetions.AuthenticationException;
import com.microservices.gateway.services.gRPCServices.OAuth2GRPCService;
import com.microservices.gateway.services.jwts.JWTService;
import com.microservices.gateway.services.sessions.OAuth2SessionService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {

    private final OAuth2GRPCService oAuth2GRPCService;
    private final JWTService jwtService;
    private final OAuth2SessionService sessionService;
    private final CookieSecuritySettings cookieSecuritySettings;

    @Value("${oauth2.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${oauth2.frontend.login-path}")
    private String frontendLoginPath;

    public OAuth2Controller(OAuth2GRPCService oAuth2GRPCService, 
                           JWTService jwtService,
                           OAuth2SessionService sessionService,
                           CookieSecuritySettings cookieSecuritySettings) {
        this.oAuth2GRPCService = oAuth2GRPCService;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
        this.cookieSecuritySettings = cookieSecuritySettings;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<OAuth2ClientRegistrationResponseDTO>> registerClient(
            @Valid @RequestBody OAuth2ClientRegistrationRequestDTO request
    ) {
        return Mono.fromCallable(() -> oAuth2GRPCService.registerClient(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    /**
     * OAuth2 authorization endpoint.
     * 
     * Flow:
     * 1. Checks if user has an active session
     * 2. If NO session → redirects to Angular frontend login page with return URL
     * 3. If session exists → issues authorization code and redirects to client's redirect_uri
     * 
     * @param responseType Must be "code" for authorization code flow
     * @param clientId OAuth2 client identifier
     * @param redirectUri Client's callback URL
     * @param scope Requested scopes (space-separated)
     * @param state CSRF protection token from client
     * @param codeChallenge PKCE code challenge (S256 hash of verifier)
     * @param codeChallengeMethod PKCE method (S256 or plain)
     * @param nonce Optional nonce for OIDC
     * @param exchange ServerWebExchange to access session
     * @return Redirect response (to login page or client redirect_uri)
     */
    @GetMapping("/authorize")
    public Mono<ResponseEntity<Void>> authorize(
            @RequestParam(name = "response_type", defaultValue = "code") String responseType,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "redirect_uri") String redirectUri,
            @RequestParam(name = "scope", required = false) String scope,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "code_challenge") String codeChallenge,
            @RequestParam(name = "code_challenge_method", defaultValue = "S256") String codeChallengeMethod,
            @RequestParam(name = "nonce", required = false) String nonce,
            ServerWebExchange exchange
    ) {
        return exchange.getSession().flatMap(session ->
                sessionService.isAuthenticated(session).flatMap(isAuthenticated -> {
                    if (!isAuthenticated) {
                        log.info("User not authenticated, redirecting to frontend login");
                        return redirectToFrontendLogin(exchange);
                    }

                    log.info("User authenticated, processing authorization request");
                    return sessionService.getEmail(session)
                            .flatMap(email -> Mono.fromCallable(() ->
                                            oAuth2GRPCService.authorize(
                                                    responseType,
                                                    clientId,
                                                    redirectUri,
                                                    scope,
                                                    state,
                                                    codeChallenge,
                                                    codeChallengeMethod,
                                                    email,
                                                    nonce
                                            ))
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .map(response -> {
                                        String locationUrl = UriComponentsBuilder.fromUriString(response.redirectUri())
                                                .queryParam("code", response.authorizationCode())
                                                .queryParam("state", response.state())
                                                .build()
                                                .toUriString();

                                        log.info("Authorization successful, redirecting to client: {}", locationUrl);
                                        return ResponseEntity.status(HttpStatus.FOUND)
                                                .location(URI.create(locationUrl))
                                                .build();
                                    }));
                })
        );
    }



    /**
     * Session logout endpoint.
     * Invalidates the OAuth2 session.
     * 
     * @param exchange ServerWebExchange for session access
     * @return Success response
     */
    @PostMapping("/session/logout")
    public Mono<ResponseEntity<Void>> logoutSession(ServerWebExchange exchange) {
        return exchange.getSession()
                .flatMap(session -> sessionService.invalidateSession(session)
                        .then(Mono.just(ResponseEntity.ok().<Void>build())));
    }

    /**
     * Redirects user to Angular frontend login page.
     * Stores the current OAuth2 request URL in session for post-login redirect.
     * 
     * @param exchange ServerWebExchange
     * @return Redirect response to Angular login
     */
    private Mono<ResponseEntity<Void>> redirectToFrontendLogin(ServerWebExchange exchange) {
        return exchange.getSession().flatMap(session -> {
            String currentUrl = exchange.getRequest().getURI().toString();
            session.getAttributes().put("oauth_return_url", currentUrl);

            String loginUrl = UriComponentsBuilder.fromHttpUrl(frontendBaseUrl)
                    .path(frontendLoginPath)
                    .queryParam("returnUrl", currentUrl)
                    .build()
                    .toUriString();

            log.info("Redirecting to frontend login: {}", loginUrl);
            return session.save().then(Mono.just(
                    ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create(loginUrl))
                            .build()
            ));
        });
    }

    @PostMapping("/token")
    public Mono<ResponseEntity<OAuth2TokenResponseDTO>> token(
            @Valid @RequestBody OAuth2TokenRequestDTO request,
            @CookieValue(name = "refresh_token", required = false) String refreshTokenCookie
    ) {
        OAuth2TokenRequestDTO effectiveRequest = request;
        if ((request.refreshToken() == null || request.refreshToken().isBlank())
                && "refresh_token".equalsIgnoreCase(request.grantType())
                && refreshTokenCookie != null && !refreshTokenCookie.isBlank()) {
            effectiveRequest = new OAuth2TokenRequestDTO(
                    request.grantType(),
                    request.code(),
                    request.redirectUri(),
                    request.codeVerifier(),
                    refreshTokenCookie,
                    request.clientId(),
                    request.clientSecret(),
                    request.scope()
            );
        }

        OAuth2TokenRequestDTO finalRequest = effectiveRequest;
        return Mono.fromCallable(() -> oAuth2GRPCService.token(finalRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> {
                    ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
                    if (response.refreshToken() != null && !response.refreshToken().isBlank()) {
                        ResponseCookie cookie = cookieSecuritySettings.applyDefaults(ResponseCookie.from("refresh_token", response.refreshToken())
                                .maxAge(Duration.ofDays(7)))
                                .build();
                        builder.header(HttpHeaders.SET_COOKIE, cookie.toString());
                    }
                    builder.header(HttpHeaders.CACHE_CONTROL, "no-store")
                            .header("Pragma", "no-cache");
                    return builder.body(response);
                });
    }

    @PostMapping("/introspect")
    public Mono<ResponseEntity<OAuth2IntrospectResponseDTO>> introspect(
            @Valid @RequestBody OAuth2IntrospectRequestDTO request
    ) {
        return Mono.fromCallable(() -> oAuth2GRPCService.introspect(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @PostMapping("/revoke")
    public Mono<ResponseEntity<OAuth2RevokeResponseDTO>> revoke(
            @Valid @RequestBody OAuth2RevokeRequestDTO request,
            @CookieValue(name = "refresh_token", required = false) String refreshTokenCookie
    ) {
        OAuth2RevokeRequestDTO effectiveRequest = request;
        if ((request.token() == null || request.token().isBlank()) && refreshTokenCookie != null && !refreshTokenCookie.isBlank()) {
            effectiveRequest = new OAuth2RevokeRequestDTO(
                    refreshTokenCookie,
                    request.tokenTypeHint() == null || request.tokenTypeHint().isBlank() ? "refresh_token" : request.tokenTypeHint(),
                    request.clientId(),
                    request.clientSecret()
            );
        }
        if (effectiveRequest.token() == null || effectiveRequest.token().isBlank()) {
            throw new AuthenticationException("token is required for revoke");
        }

        OAuth2RevokeRequestDTO finalRequest = effectiveRequest;
        return Mono.fromCallable(() -> oAuth2GRPCService.revoke(finalRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> {
                    ResponseCookie clearCookie = cookieSecuritySettings.applyDefaults(ResponseCookie.from("refresh_token", "")
                            .maxAge(Duration.ZERO))
                            .build();
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                            .header(HttpHeaders.CACHE_CONTROL, "no-store")
                            .header("Pragma", "no-cache")
                            .body(response);
                });
    }

    private void validateUuid(String value) {
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationException("Invalid user_id format", ex);
        }
    }
}
