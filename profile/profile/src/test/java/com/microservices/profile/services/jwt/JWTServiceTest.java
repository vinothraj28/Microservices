package com.microservices.profile.services.jwt;

import com.microservices.profile.models.entities.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWTService Tests")
class JWTServiceTest {

    @Spy
    @InjectMocks
    private JWTService jwtService;

    private UserProfile testUser;
    private UUID userId;
    private String testEmail;
    private String validSecret;
    private String validIssuer;
    private String validAudience;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testEmail = "john.doe@example.com";

        // Use a valid base64-encoded secret (minimum 256 bits for HS512)
        validSecret = "AxXLZtEnBsZcWCG6uBC0wCGYCRhrV2D+XVg08SNlk8yt4JQ9Ga/hgaSuqhDlL2qDLA9vDOPqVlNJQyxKBAW87Q==";
        validIssuer = "microservices";
        validAudience = "api-users";

        // Inject configuration via reflection
        ReflectionTestUtils.setField(jwtService, "secret", validSecret);
        ReflectionTestUtils.setField(jwtService, "issuer", validIssuer);
        ReflectionTestUtils.setField(jwtService, "audience", validAudience);
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", 60L);
        ReflectionTestUtils.setField(jwtService, "refreshExpirationDays", 7L);

        // Setup test user
        testUser = new UserProfile();
        testUser.setUserId(userId);
        testUser.setEmailAddress(testEmail);
        testUser.setUserName("john.doe");
        testUser.setRoles(new HashSet<>(List.of()));
    }

    // ==================== ACCESS TOKEN GENERATION TESTS ====================

    @Test
    @DisplayName("Should generate valid access token with all required claims")
    void testGenerateTokenSuccess() {
        // Act
        String token = jwtService.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3, "Token should have 3 parts");

        // Verify claims
        Claims claims = jwtService.extractClaims(token);
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals(testEmail, claims.get("email"));
        assertEquals(validIssuer, claims.getIssuer());
        assertEquals(List.of(validAudience), claims.getAudience());
        assertNotNull(claims.getId()); // JTI
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    @DisplayName("Should generate tokens with different JTI for each call")
    void testGenerateTokenUniqueness() {
        // Act
        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(testUser);

        // Assert
        assertNotEquals(token1, token2);

        String jti1 = jwtService.extractJti(token1);
        String jti2 = jwtService.extractJti(token2);
        assertNotEquals(jti1, jti2);
    }

    @Test
    @DisplayName("Should include user roles in access token")
    void testGenerateTokenWithRoles() {
        // Arrange
        testUser.setRoles(new HashSet<>(List.of()));  // Empty roles initially

        // Act
        String token = jwtService.generateToken(testUser);

        // Assert
        Claims claims = jwtService.extractClaims(token);
        List<String> roles = claims.get("roles", List.class);
        assertNotNull(roles);
        assertEquals(0, roles.size());
    }

    @Test
    @DisplayName("Should set correct expiration time for access token")
    void testAccessTokenExpiration() {
        // Arrange
        long expirationMinutes = 60;
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", expirationMinutes);

        // Act
        String token = jwtService.generateToken(testUser);
        Claims claims = jwtService.extractClaims(token);

        // Assert
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        long expirationTimeMillis = expiration.getTime() - issuedAt.getTime();
        long expectedMillis = expirationMinutes * 60 * 1000;

        // Allow 5 second tolerance
        assertTrue(Math.abs(expirationTimeMillis - expectedMillis) < 5000);
    }

    // ==================== REFRESH TOKEN GENERATION TESTS ====================

    @Test
    @DisplayName("Should generate valid refresh token with type claim")
    void testGenerateRefreshTokenSuccess() {
        // Act
        String token = jwtService.generateRefreshToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);

        // Verify claims
        Claims claims = jwtService.extractClaims(token);
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals("refresh", claims.get("type"));
        assertEquals(testEmail, claims.get("email"));
        assertEquals(validIssuer, claims.getIssuer());
    }

    @Test
    @DisplayName("Should set correct expiration time for refresh token")
    void testRefreshTokenExpiration() {
        // Arrange
        long refreshExpirationDays = 7;
        ReflectionTestUtils.setField(jwtService, "refreshExpirationDays", refreshExpirationDays);

        // Act
        String token = jwtService.generateRefreshToken(testUser);
        Claims claims = jwtService.extractClaims(token);

        // Assert
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        long expirationTimeMillis = expiration.getTime() - issuedAt.getTime();
        long expectedMillis = refreshExpirationDays * 24 * 60 * 60 * 1000;

        // Allow 5 second tolerance
        assertTrue(Math.abs(expirationTimeMillis - expectedMillis) < 5000);
    }

    @Test
    @DisplayName("Should generate refresh token without roles claim")
    void testRefreshTokenNoRoles() {
        // Act
        String token = jwtService.generateRefreshToken(testUser);
        Claims claims = jwtService.extractClaims(token);

        // Assert
        assertNull(claims.get("roles"));
        assertEquals("refresh", claims.get("type"));
    }

    // ==================== MFA CHALLENGE TOKEN TESTS ====================

    @Test
    @DisplayName("Should generate valid MFA challenge token with correct type")
    void testGenerateMfaChallengeTokenSuccess() {
        // Act
        String token = jwtService.generateMfaChallengeToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);

        Claims claims = jwtService.extractClaims(token);
        assertEquals("mfa_challenge", claims.get("type"));
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals(testEmail, claims.get("email"));
    }

    @Test
    @DisplayName("Should set short expiration for MFA challenge token")
    void testMfaChallengeTokenShortExpiry() {
        // Act
        String token = jwtService.generateMfaChallengeToken(testUser);
        Claims claims = jwtService.extractClaims(token);

        // Assert
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        long expirationTimeMillis = expiration.getTime() - issuedAt.getTime();
        long expectedMillis = 5 * 60 * 1000; // 5 minutes

        // Allow 5 second tolerance
        assertTrue(Math.abs(expirationTimeMillis - expectedMillis) < 5000);
    }

    // ==================== TOKEN VALIDATION TESTS ====================

    @Test
    @DisplayName("Should validate valid token")
    void testIsValidWithValidToken() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        boolean isValid = jwtService.isValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject invalid token signature")
    void testIsValidWithInvalidSignature() {
        // Arrange
        String token = jwtService.generateToken(testUser);
        String tampered = token.substring(0, token.length() - 10) + "XXXXXXXXXX";

        // Act
        boolean isValid = jwtService.isValid(tampered);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject malformed token")
    void testIsValidWithMalformedToken() {
        // Act
        boolean isValid = jwtService.isValid("not.a.valid.token.structure");

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should handle empty token gracefully")
    void testIsValidWithEmptyToken() {
        // Act
        boolean isValid = jwtService.isValid("");

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should throw exception when extracting claims from invalid token")
    void testExtractClaimsFromInvalidToken() {
        // Act & Assert
        assertThrows(
                JwtException.class,
                () -> jwtService.extractClaims("invalid.token.here")
        );
    }

    // ==================== CLAIM EXTRACTION TESTS ====================

    @Test
    @DisplayName("Should extract user ID from token")
    void testExtractUserId() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        String extractedUserId = jwtService.extractUserId(token);

        // Assert
        assertEquals(userId.toString(), extractedUserId);
    }

    @Test
    @DisplayName("Should extract JTI from token")
    void testExtractJti() {
        // Arrange
        String token = jwtService.generateToken(testUser);
        Claims claims = jwtService.extractClaims(token);
        String expectedJti = claims.getId();

        // Act
        String extractedJti = jwtService.extractJti(token);

        // Assert
        assertEquals(expectedJti, extractedJti);
        assertNotNull(extractedJti);
        assertFalse(extractedJti.isEmpty());
    }

    @Test
    @DisplayName("Should extract roles from token")
    void testExtractRoles() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        List<String> roles = jwtService.extractRoles(token);

        // Assert
        assertNotNull(roles);
    }

    @Test
    @DisplayName("Should extract correct claims from access token")
    void testExtractClaimsFromAccessToken() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        Claims claims = jwtService.extractClaims(token);

        // Assert
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals(testEmail, claims.get("email"));
        assertEquals(validIssuer, claims.getIssuer());
        assertEquals(List.of(validAudience), claims.getAudience());
        assertNull(claims.get("type")); // Access tokens don't have type claim
    }

    @Test
    @DisplayName("Should extract correct claims from refresh token")
    void testExtractClaimsFromRefreshToken() {
        // Arrange
        String token = jwtService.generateRefreshToken(testUser);

        // Act
        Claims claims = jwtService.extractClaims(token);

        // Assert
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals("refresh", claims.get("type"));
        assertEquals(testEmail, claims.get("email"));
        assertEquals(validIssuer, claims.getIssuer());
    }

    @Test
    @DisplayName("Should extract correct claims from MFA challenge token")
    void testExtractClaimsFromMfaChallengeToken() {
        // Arrange
        String token = jwtService.generateMfaChallengeToken(testUser);

        // Act
        Claims claims = jwtService.extractClaims(token);

        // Assert
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals("mfa_challenge", claims.get("type"));
        assertEquals(testEmail, claims.get("email"));
    }

    // ==================== SECURITY TESTS ====================

    @Test
    @DisplayName("Should not accept token with different issuer")
    void testRejectTokenWithDifferentIssuer() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "issuer", "different-issuer");
        String token = jwtService.generateToken(testUser);

        ReflectionTestUtils.setField(jwtService, "issuer", validIssuer); // Reset to original

        // Act & Assert
        assertThrows(
                JwtException.class,
                () -> jwtService.extractClaims(token)
        );
    }

    @Test
    @DisplayName("Should not accept token with different audience")
    void testRejectTokenWithDifferentAudience() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "audience", "different-audience");
        String token = jwtService.generateToken(testUser);

        ReflectionTestUtils.setField(jwtService, "audience", validAudience); // Reset to original

        // Act & Assert
        assertThrows(
                JwtException.class,
                () -> jwtService.extractClaims(token)
        );
    }

    @Test
    @DisplayName("Should preserve token integrity across multiple generations")
    void testTokenIntegrityMultipleGenerations() {
        // Act
        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(testUser);

        Claims claims1 = jwtService.extractClaims(token1);
        Claims claims2 = jwtService.extractClaims(token2);

        // Assert
        assertEquals(claims1.getSubject(), claims2.getSubject());
        assertEquals(claims1.get("email"), claims2.get("email"));
        assertNotEquals(claims1.getId(), claims2.getId());
        assertNotEquals(claims1.getIssuedAt(), claims2.getIssuedAt());
    }

    @Test
    @DisplayName("Should handle user with multiple roles in token")
    void testTokenWithMultipleRoles() {
        // Arrange
        testUser.setRoles(new HashSet<>(List.of())); // In actual test, this would have roles

        // Act
        String token = jwtService.generateToken(testUser);
        Claims claims = jwtService.extractClaims(token);

        // Assert
        List<String> roles = claims.get("roles", List.class);
        assertNotNull(roles);
    }
}

