package com.microservices.profile.services.jwt;

import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JWTService implements TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.expiration-minutes:60}")
    private long expirationMinutes;

    @Value("${jwt.refresh-expiration-days:7}")
    private long refreshExpirationDays;

    private static final long MILLIS_PER_MINUTE = 60_000L;
    private static final long MFA_CHALLENGE_EXPIRY_MINUTES = 5;

    public String generateToken(UserProfile userProfile) {
        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (expirationMinutes * MILLIS_PER_MINUTE));

        return Jwts.builder()
                .issuer(issuer)
                .audience().add(audience).and()
                .subject(userProfile.getUserId().toString())
                .id(jti)
                .claim("email", userProfile.getEmailAddress())
                .claim("roles", userProfile.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();
    }

    /**
     * Generates a long-lived refresh token. Refresh tokens are marked with claim type="refresh"
     * to prevent accidental use as access tokens.
     */
    @Override
    public String generateRefreshToken(UserProfile userProfile) {
        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (refreshExpirationDays * 24 * 60 * MILLIS_PER_MINUTE));

        return Jwts.builder()
                .issuer(issuer)
                .audience().add(audience).and()
                .subject(userProfile.getUserId().toString())
                .id(jti)
                .claim("type", "refresh")
                .claim("email", userProfile.getEmailAddress())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .requireAudience(audience)
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractJti(String token) {
        return extractClaims(token).getId();
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    public boolean isValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Generates a short-lived MFA challenge token.
     *
     * This token is used for the MFA verification step and should NOT be used as an access token.
     * It is marked with type="mfa_challenge" to prevent token type confusion.
     *
     * Expiry: 5 minutes (user must complete MFA verification within this window)
     *
     * @param userProfile User to create challenge token for
     * @return MFA challenge JWT token with short expiry
     */
    @Override
    public String generateMfaChallengeToken(UserProfile userProfile) {
        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (MFA_CHALLENGE_EXPIRY_MINUTES * MILLIS_PER_MINUTE));

        return Jwts.builder()
                .issuer(issuer)
                .audience().add(audience).and()
                .subject(userProfile.getUserId().toString())
                .id(jti)
                .claim("type", "mfa_challenge")  // Mark token type to prevent misuse
                .claim("email", userProfile.getEmailAddress())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
