package com.microservices.gateway.services.jwts;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .requireIssuer(issuer)
                    .requireAudience(audience)
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Failed to extract claims from token: {}", e.getMessage());
            throw e;
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    public boolean isValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date(System.currentTimeMillis()));
        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
