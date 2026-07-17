package com.microservices.gateway.configurations;

import com.microservices.gateway.filters.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterAt(
                        jwtFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/authenticate").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/verify-mfa").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                        .pathMatchers("/actuator/health/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyExchange().authenticated()
                )
                .headers(headers -> headers
                .contentSecurityPolicy(csp ->
                        csp.policyDirectives(
                                "default-src 'self'; script-src 'self'"
                        )
                )
                .frameOptions(frameOptions ->
                        frameOptions.mode(
                                XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN
                        )
                    )
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
