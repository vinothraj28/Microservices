package com.microservices.profile.jwt;

import com.microservices.profile.services.TokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JWTFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!tokenService.isValid(token)) {
                log.warn("Invalid or expired token received");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            log.debug("Token validation successful, extracting claims");
            String email = tokenService.extractClaims(token).get("email", String.class);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of()
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException ex) {
            log.error("JWT validation error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
