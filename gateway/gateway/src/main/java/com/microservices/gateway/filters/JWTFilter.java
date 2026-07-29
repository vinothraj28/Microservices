package com.microservices.gateway.filters;

import com.microservices.gateway.services.jwts.JWTService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JWTFilter implements WebFilter {

    private final JWTService jwtService;

    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.warn("Processing request for path: {} with Authorization header: {}", path, authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            if (!jwtService.isValid(token)) {
                log.warn("Invalid or expired token for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String email = jwtService.extractEmail(token);
            List<String> roles = jwtService.extractRoles(token);

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            roles.stream().map(SimpleGrantedAuthority::new
                            ).toList()
                    );

            String rolesHeader = String.join(",", roles != null ? roles : List.of());
            exchange = exchange.mutate()
                    .request(r -> r.headers(httpHeaders -> {
                        httpHeaders.remove("X-User-Email");
                        httpHeaders.remove("X-User-Roles");
                        httpHeaders.add("X-User-Email", email);
                        httpHeaders.add("X-User-Roles", rolesHeader);
                    }))
                    .build();

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (JwtException ex) {
            log.error("JWT validation failed: {}", ex.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/v1/users/login") ||
               path.equals("/api/v1/users/register") ||
               path.equals("/api/v1/auth/authenticate") ||
               path.equals("/api/v1/auth/verify-mfa") ||
               path.contains("/api/v1/oauth2") ||
               path.contains("/api/v1/auth/oAuth") ||
                path.contains("/api/v1/mfa") ||
                path.contains("/api/v1/movies/");
    }

}
