package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.auth.AuthenticationRequestDTO;
import com.microservices.gateway.DTOS.auth.AuthenticationResponseDTO;
import com.microservices.gateway.DTOS.auth.RefreshTokenResponseDTO;
import com.microservices.gateway.DTOS.mfa.MFAOAuthVerificationResponseDTO;
import com.microservices.gateway.DTOS.mfa.MfaVerificationRequestDTO;
import com.microservices.gateway.DTOS.mfa.MfaVerificationResponseDTO;
import com.microservices.gateway.configurations.CookieSecuritySettings;
import com.microservices.gateway.services.gRPCServices.AuthenticationGRPCService;
import com.microservices.gateway.services.sessions.OAuth2SessionService;
import com.microservices.profile.grpc.LogoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.net.URI;
import java.time.Duration;

/**
 * REST Controller for Authentication flows.
 *
 * Gateway entry point for the Profile Service's Authentication endpoints.
 * Handles client requests and delegates to AuthenticationGRPCService for gRPC communication.
 *
 * Exposes two endpoints:
 * 1. POST /api/v1/auth/authenticate - Initial credential validation
 * 2. POST /api/v1/auth/verify-mfa - MFA code verification
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication APIs", description = "User authentication and MFA verification endpoints (gateway)")
public class AuthenticationController {

    private final AuthenticationGRPCService authenticationGRPCService;
    private final OAuth2SessionService sessionService;
    private final CookieSecuritySettings cookieSecuritySettings;

    public AuthenticationController(AuthenticationGRPCService authenticationGRPCService,
                                    OAuth2SessionService sessionService,
                                    CookieSecuritySettings cookieSecuritySettings) {
        this.authenticationGRPCService = authenticationGRPCService;
        this.sessionService = sessionService;
        this.cookieSecuritySettings = cookieSecuritySettings;
    }

//    @Operation(
//            summary = "Authenticate user with email and password",
//            description = "Initiates authentication. Returns JWT token if MFA not required, or MFA challenge token if MFA verification needed."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Authentication successful",
//                    content = @Content(schema = @Schema(implementation = AuthenticationResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "401",
//                    description = "Invalid email or password"
//            ),
//            @ApiResponse(
//                    responseCode = "422",
//                    description = "Validation failed (invalid email format, blank fields)"
//            )
//    })
//    @PostMapping("/authenticate")
//    public Mono<ResponseEntity<AuthenticationResponseDTO>> authenticate(
//            @Valid @RequestBody AuthenticationRequestDTO authRequest
//    ) {
//        log.info("Authentication request received for email: {}", authRequest.email());
//
//        return Mono.fromCallable(() -> authenticationGRPCService.authenticate(authRequest))
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMap(response -> {
//                    if (response.mfaRequired()) {
//                        log.info("MFA required for email: {}", authRequest.email());
//                        ResponseCookie cookie = ResponseCookie.from("mfa_challenge_token", response.mfaChallengeToken())
//                                .httpOnly(true)
//                                .secure(false) //true for production
//                                .sameSite("Strict")
//                                .path("/")
//                                .maxAge(Duration.ofMinutes(5))
//                                .build();
//                        return Mono.just(ResponseEntity.ok()
//                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                                .body(response));
//                    } else {
//                        log.info("Authentication successful (no MFA) for email: {}", authRequest.email());
//                        // If a refresh token is present, set it as an HttpOnly, Secure, SameSite=Strict cookie
//                        if (response.refreshToken() != null) {
//                            ResponseCookie cookie = ResponseCookie.from("refresh_token", response.refreshToken())
//                                    .httpOnly(true)
//                                    .secure(false) //true for production
//                                    .sameSite("Strict")
//                                    .path("/")
//                                    .maxAge(Duration.ofDays(7))
//                                    .build();
//
//                            //return the refresh token only in the cookies
//                            AuthenticationResponseDTO authenticationResponseDTO =
//                                    AuthenticationResponseDTO.successWithoutMfa(response.accessToken());
//
//                            return Mono.just(ResponseEntity.ok()
//                                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                                    .body(authenticationResponseDTO));
//                        }
//
//                        return Mono.just(ResponseEntity.ok(response));
//                    }
//                })
//                .doOnError(error -> log.warn("Authentication failed: {}", error.getMessage()));
//    }

    @PostMapping("/oAuth/authenticate")
    public Mono<ResponseEntity<AuthenticationResponseDTO>> authenticateOauth(
            @Valid @RequestBody AuthenticationRequestDTO authRequest,
            ServerWebExchange exchange
    ) {
        log.info("Authentication request received for email: {}", authRequest.email());
        return Mono.fromCallable(() ->
                        authenticationGRPCService.authenticate(authRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(response -> {
                    if (response.mfaRequired()) {
                        log.info("MFA required for {}", authRequest.email());
                        ResponseCookie challengeCookie = cookieSecuritySettings.applyDefaults(ResponseCookie.from(
                                        "mfa_challenge_token",
                                        response.mfaChallengeToken())
                                .maxAge(Duration.ofMinutes(5)))
                                .build();

                        return exchange.getSession()
                                .flatMap(session ->
                                        sessionService.createPendingSession(session, authRequest.email())
                                                .thenReturn(
                                                        ResponseEntity.ok()
                                                                .header(HttpHeaders.SET_COOKIE, challengeCookie.toString())
                                                                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                                                .header("Pragma", "no-cache")
                                                                .body(response)
                                                )
                                );
                    }
                    log.info("Authentication successful for {}", authRequest.email());
                   return Mono.just(ResponseEntity.ok()
                           .header(HttpHeaders.CACHE_CONTROL, "no-store")
                           .header("Pragma", "no-cache")
                           .body(response));

                });

//                    return exchange.getSession()
//                            .flatMap(session ->
//                                    sessionService
//                                            .establishSession(
//                                                    session,
//                                                    authRequest.email())
//                                            .then(sessionService.getReturnUrl(session))
//                                            .flatMap(returnUrl ->
//                                                    sessionService
//                                                            .clearReturnUrl(session)
//                                                            .thenReturn(
//                                                                    ResponseEntity.status(HttpStatus.FOUND)
//                                                                            .location(URI.create(returnUrl))
//                                                                            .build()
//                                                            )
//                                            )
//                            );
//                });
    }

    @PostMapping("/oAuth/verify-mfa")
    public Mono<ResponseEntity<MFAOAuthVerificationResponseDTO>> verifyMfaOauth(
            @Valid @RequestBody MfaVerificationRequestDTO mfaRequest,
            @CookieValue("mfa_challenge_token") String mfaChallengeToken,
            ServerWebExchange exchange
    ) {
        MfaVerificationRequestDTO requestDTO = MfaVerificationRequestDTO.from(mfaChallengeToken, mfaRequest.mfaCode());
        return Mono.fromCallable(() ->
                        authenticationGRPCService.verifyMfa(requestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(response ->
                        exchange.getSession().flatMap(session ->
                                        sessionService.getEmail(session).switchIfEmpty(
                                                        Mono.error(
                                                                new IllegalStateException(
                                                                        "Email not found in session."
                                                                )
                                                        )
                                                )
                                                .flatMap(email -> sessionService.markAuthenticated(session).thenReturn(email))
                                                .then(sessionService.getReturnUrl(session))
                                                .flatMap(returnUrl -> sessionService.clearReturnUrl(session)
                                                        .thenReturn(
                                                                        ResponseEntity.status(HttpStatus.OK)
                                                                                .header(HttpHeaders.SET_COOKIE, cookieSecuritySettings
                                                                                        .applyDefaults(ResponseCookie.from("mfa_challenge_token", "")
                                                                                                .maxAge(Duration.ZERO))
                                                                                        .build()
                                                                                        .toString())
                                                                                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                                                                .header("Pragma", "no-cache")
                                                                                .body(new MFAOAuthVerificationResponseDTO(returnUrl))
                                                                )
                                                )
                                ));
    }

//    @Operation(
//            summary = "Verify MFA code and complete authentication",
//            description = "Completes the authentication process by verifying MFA code. Returns JWT token on success."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "MFA verification successful, JWT token returned",
//                    content = @Content(schema = @Schema(implementation = MfaVerificationResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "401",
//                    description = "Invalid MFA code or challenge token"
//            ),
//            @ApiResponse(
//                    responseCode = "422",
//                    description = "Validation failed (blank fields)"
//            )
//    })
//    @PostMapping("/verify-mfa")
//    public Mono<ResponseEntity<MfaVerificationResponseDTO>> verifyMfa(
//            @Valid @RequestBody MfaVerificationRequestDTO mfaRequest,
//            @CookieValue("mfa_challenge_token") String mfaChallengeToken
//    ) {
//
//        log.debug("MFA verification request received");
//        MfaVerificationRequestDTO requestDTO = MfaVerificationRequestDTO.from(mfaChallengeToken, mfaRequest.mfaCode());
//
//        return Mono.fromCallable(() -> authenticationGRPCService.verifyMfa(requestDTO))
//                .subscribeOn(Schedulers.boundedElastic())
//                .doOnNext(response -> log.info("MFA verification successful, JWT token issued"))
//                .map(ResponseEntity::ok)
//                .onErrorResume(error -> {
//                    log.warn("MFA verification failed: {}", error.getMessage());
//                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//                });
//    }

    @Operation(summary = "Refresh JWT token using refresh token", description = "Refreshes the JWT access token using a valid refresh token. Returns new access token and sets new refresh token in HttpOnly cookie.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = RefreshTokenResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token"
            )
    }
    )
    @PostMapping("/refresh")
    public Mono<ResponseEntity<RefreshTokenResponseDTO>> refreshToken( @CookieValue("refresh_token")String refreshToken) {
        log.info("Refresh token request received");
        return Mono.fromCallable(() -> authenticationGRPCService.refreshToken(refreshToken))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> {
                    ResponseCookie cookie = cookieSecuritySettings.applyDefaults(ResponseCookie.from("refresh_token", response.refreshToken())
                                    .maxAge(Duration.ofDays(7)))
                            .build();

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .header(HttpHeaders.CACHE_CONTROL, "no-store")
                            .header("Pragma", "no-cache")
                            .body(response);
                })
                .doOnError(error -> log.warn("Refresh token failed: {}", error.getMessage()));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<LogoutResponse>> logout(@CookieValue("refresh_token") String refreshToken) {
        log.info("Logout request received");
        return Mono.fromCallable(() -> authenticationGRPCService.logout(refreshToken))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok
                )
                .doOnError(error -> log.warn("Logout failed: {}", error.getMessage()));
    }
}
