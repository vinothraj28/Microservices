package com.microservices.profile.services.Impl;

import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.models.entities.RefreshToken;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.Roles;
import com.microservices.profile.repository.RefreshTokenRepository;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.spi.managers.UserManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenServiceImpl Tests")
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserManager userManager;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private UserProfile testUser;
    private UUID userId;
    private String testEmail;
    private String refreshToken;
    private String jti;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testEmail = "john.doe@example.com";
        jti = UUID.randomUUID().toString();
        refreshToken = "valid.refresh.token";

        // Setup test user
        testUser = new UserProfile();
        testUser.setUserId(userId);
        testUser.setEmailAddress(testEmail);
        testUser.setUserName("john.doe");
        testUser.setRoles(new HashSet<>(List.of()));
    }

    // ==================== SAVE REFRESH TOKEN TESTS ====================

    @Test
    @DisplayName("Should save refresh token successfully")
    void testSaveRefreshTokenSuccess() {
        // Arrange
        Date expiration = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getExpiration()).thenReturn(expiration);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);

        // Act
        refreshTokenService.saveRefreshToken(userId, refreshToken);

        // Assert
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken savedToken = captor.getValue();
        assertEquals(userId, savedToken.getUserId());
        assertEquals(jti, savedToken.getJti());
        assertEquals(refreshToken, savedToken.getToken());
        assertFalse(savedToken.isRevoked());
        assertNotNull(savedToken.getExpirationTime());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when JWT extraction fails")
    void testSaveRefreshTokenInvalidFormat() {
        // Arrange
        JwtException jwtException = new JwtException("Invalid token");
        when(tokenService.extractClaims(refreshToken)).thenThrow(jwtException);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.saveRefreshToken(userId, refreshToken)
        );

        assertEquals("Invalid refresh token format", exception.getMessage());
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should calculate correct expiration time from JWT")
    void testSaveRefreshTokenExpirationCalculation() {
        // Arrange
        long futureTimeMs = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000;
        Date expiration = new Date(futureTimeMs);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getExpiration()).thenReturn(expiration);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);

        // Act
        refreshTokenService.saveRefreshToken(userId, refreshToken);

        // Assert
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken savedToken = captor.getValue();
        LocalDateTime expectedTime = Instant.ofEpochMilli(futureTimeMs)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Allow 1 second tolerance
        assertTrue(Math.abs(savedToken.getExpirationTime().getSecond() - expectedTime.getSecond()) <= 1);
    }

    // ==================== REFRESH ACCESS TOKEN TESTS ====================

    @Test
    @DisplayName("Should refresh access token successfully")
    void testRefreshAccessTokenSuccess() {
        // Arrange
        String newAccessToken = "new.access.token";
        String newRefreshToken = "new.refresh.token";

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId(userId);
        storedToken.setJti(jti);
        storedToken.setToken(refreshToken);
        storedToken.setExpirationTime(LocalDateTime.now().plusDays(7));
        storedToken.setRevoked(false);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.of(storedToken));
        when(userManager.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(tokenService.generateToken(testUser)).thenReturn(newAccessToken);
        when(tokenService.generateRefreshToken(testUser)).thenReturn(newRefreshToken);

        // Act
        var result = refreshTokenService.refreshAccessToken(refreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(newAccessToken, result.accessToken());
        assertEquals(newRefreshToken, result.refreshToken());

        verify(tokenService).generateToken(testUser);
        verify(tokenService).generateRefreshToken(testUser);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when token type is not refresh")
    void testRefreshAccessTokenInvalidType() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("access"); // Wrong type

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertEquals("Token is not a valid refresh token", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when token type is missing")
    void testRefreshAccessTokenMissingType() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn(null); // Missing type

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertEquals("Token is not a valid refresh token", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user ID is invalid UUID format")
    void testRefreshAccessTokenInvalidUserId() {
        // Arrange
        String invalidUserId = "not-a-uuid";
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(invalidUserId);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertEquals("Invalid refresh token", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when token not found in database")
    void testRefreshAccessTokenNotFound() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertTrue(exception.getMessage().contains("revoked or is invalid"));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when token is revoked")
    void testRefreshAccessTokenRevoked() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        RefreshToken revokedToken = new RefreshToken();
        revokedToken.setUserId(userId);
        revokedToken.setJti(jti);
        revokedToken.setToken(refreshToken);
        revokedToken.setExpirationTime(LocalDateTime.now().plusDays(7));
        revokedToken.setRevoked(true);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.of(revokedToken));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertTrue(exception.getMessage().contains("expired or revoked"));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when token is expired")
    void testRefreshAccessTokenExpired() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setUserId(userId);
        expiredToken.setJti(jti);
        expiredToken.setToken(refreshToken);
        expiredToken.setExpirationTime(LocalDateTime.now().minusDays(1)); // Expired
        expiredToken.setRevoked(false);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.of(expiredToken));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertTrue(exception.getMessage().contains("expired"));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user not found")
    void testRefreshAccessTokenUserNotFound() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId(userId);
        storedToken.setJti(jti);
        storedToken.setToken(refreshToken);
        storedToken.setExpirationTime(LocalDateTime.now().plusDays(7));
        storedToken.setRevoked(false);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.of(storedToken));
        when(userManager.getUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when JWT parsing fails")
    void testRefreshAccessTokenJwtException() {
        // Arrange
        JwtException jwtException = new JwtException("Invalid token");
        when(tokenService.extractClaims(refreshToken)).thenThrow(jwtException);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> refreshTokenService.refreshAccessToken(refreshToken)
        );

        assertTrue(exception.getMessage().contains("Invalid or expired refresh token"));
    }

    @Test
    @DisplayName("Should revoke old token and save new token during refresh")
    void testRefreshAccessTokenRevokesOldToken() {
        // Arrange
        String newAccessToken = "new.access.token";
        String newRefreshToken = "new.refresh.token";
        Date newExpiration = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("refresh");
        when(mockClaims.getSubject()).thenReturn(userId.toString());
        when(mockClaims.getExpiration()).thenReturn(newExpiration);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId(userId);
        storedToken.setJti(jti);
        storedToken.setToken(refreshToken);
        storedToken.setExpirationTime(LocalDateTime.now().plusDays(7));
        storedToken.setRevoked(false);

        when(tokenService.extractClaims(refreshToken)).thenReturn(mockClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.of(storedToken));
        when(userManager.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(tokenService.generateToken(testUser)).thenReturn(newAccessToken);
        when(tokenService.generateRefreshToken(testUser)).thenReturn(newRefreshToken);

        // Act
        var result = refreshTokenService.refreshAccessToken(refreshToken);

        // Assert
        assertTrue(storedToken.isRevoked()); // Old token should be revoked
        verify(refreshTokenRepository, times(2)).save(any()); // Old token revoke + new token save

        assertEquals(newAccessToken, result.accessToken());
        assertEquals(newRefreshToken, result.refreshToken());
    }

    // ==================== REVOKE REFRESH TOKEN TESTS ====================

    @Test
    @DisplayName("Should revoke refresh token successfully")
    void testRevokeRefreshTokenSuccess() {
        // Arrange
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);

        // Act
        refreshTokenService.revokeRefreshToken(refreshToken);

        // Assert
        verify(refreshTokenRepository).revokeByJti(jti);
    }

    @Test
    @DisplayName("Should handle JWT extraction failure during revocation gracefully")
    void testRevokeRefreshTokenJwtException() {
        // Arrange
        JwtException jwtException = new JwtException("Invalid token");
        when(tokenService.extractJti(refreshToken)).thenThrow(jwtException);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> refreshTokenService.revokeRefreshToken(refreshToken));

        verify(refreshTokenRepository, never()).revokeByJti(any());
    }

    // ==================== REVOKE ALL TOKENS TESTS ====================

    @Test
    @DisplayName("Should revoke all tokens for user")
    void testRevokeAllTokensForUserSuccess() {
        // Act
        refreshTokenService.revokeAllTokensForUser(userId);

        // Assert
        verify(refreshTokenRepository).revokeAllByUserId(userId);
    }

    @Test
    @DisplayName("Should handle multiple revocations for same user")
    void testRevokeAllTokensMultipleCalls() {
        // Act
        refreshTokenService.revokeAllTokensForUser(userId);
        refreshTokenService.revokeAllTokensForUser(userId);

        // Assert
        verify(refreshTokenRepository, times(2)).revokeAllByUserId(userId);
    }

    // ==================== INTEGRATION-LIKE TESTS ====================

    @Test
    @DisplayName("Should handle complete token lifecycle: save -> validate -> refresh -> revoke")
    void testCompleteTokenLifecycle() {
        // Step 1: Save token
        Date expiration = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        Claims saveClaims = mock(Claims.class);
        when(saveClaims.getExpiration()).thenReturn(expiration);

        when(tokenService.extractClaims(refreshToken)).thenReturn(saveClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);

        refreshTokenService.saveRefreshToken(userId, refreshToken);

        verify(refreshTokenRepository).save(any(RefreshToken.class));

        // Step 2: Refresh token
        String newAccessToken = "new.access.token";
        String newRefreshToken = "new.refresh.token";

        Claims refreshClaims = mock(Claims.class);
        when(refreshClaims.get("type")).thenReturn("refresh");
        when(refreshClaims.getSubject()).thenReturn(userId.toString());
        when(refreshClaims.getExpiration()).thenReturn(expiration);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId(userId);
        storedToken.setJti(jti);
        storedToken.setToken(refreshToken);
        storedToken.setExpirationTime(LocalDateTime.now().plusDays(7));
        storedToken.setRevoked(false);

        when(tokenService.extractClaims(refreshToken)).thenReturn(refreshClaims);
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        when(refreshTokenRepository.findByJtiAndUserId(jti, userId))
                .thenReturn(Optional.of(storedToken));
        when(userManager.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(tokenService.generateToken(testUser)).thenReturn(newAccessToken);
        when(tokenService.generateRefreshToken(testUser)).thenReturn(newRefreshToken);

        var result = refreshTokenService.refreshAccessToken(refreshToken);

        assertEquals(newAccessToken, result.accessToken());
        assertEquals(newRefreshToken, result.refreshToken());

        // Step 3: Revoke old token
        when(tokenService.extractJti(refreshToken)).thenReturn(jti);
        refreshTokenService.revokeRefreshToken(refreshToken);

        verify(refreshTokenRepository).revokeByJti(jti);
    }
}

