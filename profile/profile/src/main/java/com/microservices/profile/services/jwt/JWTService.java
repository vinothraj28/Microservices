package com.microservices.profile.services.jwt;

import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.TableOwner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JWTService implements TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final static long EXPIRATION = 1000 * 60 * 60;


    public String generateToken(UserProfile userProfile){

        return Jwts.builder()
                .subject(userProfile.getUserId().toString())
                .claim("email", userProfile.getEmailAddress())
                .claim("roles", userProfile.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserId(String token){
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    public boolean isValid(String token){
        return extractClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
