package com.microservices.profile.services;

import com.microservices.profile.models.entities.UserProfile;
import io.jsonwebtoken.Claims;

public interface TokenService {

    String generateToken(UserProfile userProfile);
    Claims extractClaims(String token);
    String extractUserId(String token);
    public boolean isValid(String token);

}
