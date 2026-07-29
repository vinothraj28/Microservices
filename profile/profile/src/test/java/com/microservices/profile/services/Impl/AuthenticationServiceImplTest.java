package com.microservices.profile.services.Impl;

import com.microservices.profile.dto.auth.AuthenticationRequestDTO;
import com.microservices.profile.dto.auth.AuthenticationResponseDTO;
import com.microservices.profile.dto.auth.MFAVerificationRequestDTO;
import com.microservices.profile.dto.auth.MFAVerificationResponseDTO;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.InvalidMfaChallengeException;
import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.services.RefreshTokenService;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.spi.managers.CredentialsManager;
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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationServiceImpl Tests")
class AuthenticationServiceImplTest {

    @Mock
    private UserManager userManager;

    @Mock
    private CredentialsManager credentialsManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private UserProfile testUser;
    private UUID userId;
    private String testEmail;
    private String testPassword;
    private String accessToken;
    private String refreshToken;
    private String mfaChallengeToken;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testEmail = "john.doe@example.com";
        testPassword = "TestPassword123!";
        accessToken = "valid.access.token";
        refreshToken = "valid.refresh.token";
        mfaChallengeToken = "valid.mfa.challenge.token";

        // Setup test user
        testUser = new UserProfile();
        testUser.setUserId(userId);
        testUser.setEmailAddress(testEmail);
        testUser.setUserName("john.doe");
        testUser.setRoles(new HashSet<>(List.of()));
    }

    // ==================== AUTHENTICATE TESTS ====================

    @Test
    @DisplayName("Should successfully authenticate user without MFA")
    void testAuthenticateSuccessfulWithoutMfa() {
        // Arrange
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(testEmail, testPassword);
        when(userManager.getUserByEmailAddress(testEmail)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.PASSWORD, testPassword))
                .thenReturn(true);
        when(credentialsManager.getActiveCredential(testUser, CredentialsType.TOTP))
                .thenReturn(Optional.empty());
        when(tokenService.generateToken(testUser)).thenReturn(accessToken);
        when(tokenService.generateRefreshToken(testUser)).thenReturn(refreshToken);

        // Act
        AuthenticationResponseDTO response = authenticationService.authenticate(authRequest);

        // Assert
       // assertNotNull(response);
       // assertTrue(response.success());
        assertEquals(accessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
        assertNull(response.mfaChallengeToken());

        // Verify
        verify(userManager).getUserByEmailAddress(testEmail);
        verify(credentialsManager).validateCredential(testUser, CredentialsType.PASSWORD, testPassword);
        verify(credentialsManager).getActiveCredential(testUser, CredentialsType.TOTP);
        verify(tokenService).generateToken(testUser);
        verify(tokenService).generateRefreshToken(testUser);
        verify(refreshTokenService).saveRefreshToken(userId, refreshToken);
    }

    @Test
    @DisplayName("Should return MFA challenge token when MFA is enabled")
    void testAuthenticateSuccessfulWithMfaRequired() {
        // Arrange
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(testEmail, testPassword);
        Credential mfaCredential = new Credential();
        //mfaCredential.setCredentialType(CredentialsType.TOTP);
        mfaCredential.setStatus(CredentialStatus.ACTIVE);

        when(userManager.getUserByEmailAddress(testEmail)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.PASSWORD, testPassword))
                .thenReturn(true);
        when(credentialsManager.getActiveCredential(testUser, CredentialsType.TOTP))
                .thenReturn(Optional.of(mfaCredential));
        when(tokenService.generateMfaChallengeToken(testUser)).thenReturn(mfaChallengeToken);

        // Act
        AuthenticationResponseDTO response = authenticationService.authenticate(authRequest);

        // Assert
        assertNotNull(response);
        //assertFalse(response.success());
        assertNull(response.accessToken());
        assertNull(response.refreshToken());
        assertEquals(mfaChallengeToken, response.mfaChallengeToken());

        // Verify - refresh token should not be saved when MFA is required
        verify(refreshTokenService, never()).saveRefreshToken(any(), any());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user not found")
    void testAuthenticateUserNotFound() {
        // Arrange
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(testEmail, testPassword);
        when(userManager.getUserByEmailAddress(testEmail)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authenticationService.authenticate(authRequest)
        );

        assertEquals("Email or password is incorrect", exception.getMessage());
        verify(userManager).getUserByEmailAddress(testEmail);
        verify(credentialsManager, never()).validateCredential(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is invalid")
    void testAuthenticateInvalidPassword() {
        // Arrange
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(testEmail, testPassword);
        when(userManager.getUserByEmailAddress(testEmail)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.PASSWORD, testPassword))
                .thenReturn(false);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authenticationService.authenticate(authRequest)
        );

        assertEquals("Email or password is incorrect", exception.getMessage());
        verify(credentialsManager, never()).getActiveCredential(any(), any());
    }

    // ==================== VERIFY MFA TESTS ====================

    @Test
    @DisplayName("Should successfully verify MFA code and return JWT tokens")
    void testVerifyMfaSuccessful() {
        // Arrange
        String mfaCode = "123456";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        // Setup mock claims for the MFA challenge token
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("mfa_challenge");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        when(tokenService.extractClaims(mfaChallengeToken)).thenReturn(mockClaims);
        when(userManager.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.TOTP, mfaCode))
                .thenReturn(true);
        when(tokenService.generateToken(testUser)).thenReturn(accessToken);
        when(tokenService.generateRefreshToken(testUser)).thenReturn(refreshToken);

        // Act
        MFAVerificationResponseDTO response = authenticationService.verifyMfa(mfaRequest);

        // Assert
        assertNotNull(response);
        //assertTrue(response.success());
        assertEquals(accessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());

        // Verify
        verify(tokenService).extractClaims(mfaChallengeToken);
        verify(userManager).getUserById(userId);
        verify(credentialsManager).validateCredential(testUser, CredentialsType.TOTP, mfaCode);
        verify(refreshTokenService).saveRefreshToken(userId, refreshToken);
    }

    @Test
    @DisplayName("Should throw InvalidMfaChallengeException when token type is invalid")
    void testVerifyMfaInvalidTokenType() {
        // Arrange
        String mfaCode = "123456";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("access");  // Wrong type

        when(tokenService.extractClaims(mfaChallengeToken)).thenReturn(mockClaims);

        // Act & Assert
        InvalidMfaChallengeException exception = assertThrows(
                InvalidMfaChallengeException.class,
                () -> authenticationService.verifyMfa(mfaRequest)
        );

        assertEquals("Invalid MFA challenge token", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidMfaChallengeException when challenge token is expired")
    void testVerifyMfaExpiredToken() {
        // Arrange
        String mfaCode = "123456";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        JwtException jwtException = new JwtException("Token expired");
        when(tokenService.extractClaims(mfaChallengeToken)).thenThrow(jwtException);

        // Act & Assert
        InvalidMfaChallengeException exception = assertThrows(
                InvalidMfaChallengeException.class,
                () -> authenticationService.verifyMfa(mfaRequest)
        );

        assertTrue(exception.getMessage().contains("invalid or expired"));
    }

    @Test
    @DisplayName("Should throw InvalidMfaChallengeException when user not found from challenge token")
    void testVerifyMfaUserNotFound() {
        // Arrange
        String mfaCode = "123456";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("mfa_challenge");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        when(tokenService.extractClaims(mfaChallengeToken)).thenReturn(mockClaims);
        when(userManager.getUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidMfaChallengeException exception = assertThrows(
                InvalidMfaChallengeException.class,
                () -> authenticationService.verifyMfa(mfaRequest)
        );

        assertTrue(exception.getMessage().contains("User context not found"));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when MFA code is invalid")
    void testVerifyMfaInvalidCode() {
        // Arrange
        String mfaCode = "000000";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("mfa_challenge");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        when(tokenService.extractClaims(mfaChallengeToken)).thenReturn(mockClaims);
        when(userManager.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.TOTP, mfaCode))
                .thenReturn(false);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authenticationService.verifyMfa(mfaRequest)
        );

        assertEquals("Invalid MFA code", exception.getMessage());
        verify(refreshTokenService, never()).saveRefreshToken(any(), any());
    }

    @Test
    @DisplayName("Should throw InvalidMfaChallengeException when userId is invalid format")
    void testVerifyMfaInvalidUserIdFormat() {
        // Arrange
        String mfaCode = "123456";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("mfa_challenge");
        when(mockClaims.getSubject()).thenReturn("invalid-uuid-format");

        when(tokenService.extractClaims(mfaChallengeToken)).thenReturn(mockClaims);

        // Act & Assert
        InvalidMfaChallengeException exception = assertThrows(
                InvalidMfaChallengeException.class,
                () -> authenticationService.verifyMfa(mfaRequest)
        );

        assertEquals("Invalid MFA challenge token", exception.getMessage());
    }

    // ==================== LOGOUT TESTS ====================

    @Test
    @DisplayName("Should successfully logout user")
    void testLogoutSuccessful() {
        // Act
        authenticationService.logout(refreshToken);

        // Assert
        verify(refreshTokenService).revokeRefreshToken(refreshToken);
    }

    @Test
    @DisplayName("Should handle logout failure gracefully")
    void testLogoutWithException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(refreshTokenService)
                .revokeRefreshToken(refreshToken);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> authenticationService.logout(refreshToken));

        verify(refreshTokenService).revokeRefreshToken(refreshToken);
    }

    // ==================== REFRESH TOKEN TESTS ====================

    @Test
    @DisplayName("Should successfully refresh access token")
    void testRefreshAccessTokenSuccessful() {
        // Arrange
        String newAccessToken = "new.access.token";
        String newRefreshToken = "new.refresh.token";
        RefreshTokenService.RefreshTokenResult result = new RefreshTokenService.RefreshTokenResult(
                newAccessToken, newRefreshToken
        );

        when(refreshTokenService.refreshAccessToken(refreshToken)).thenReturn(result);

        // Act
        AuthenticationResponseDTO response = authenticationService.refreshAccessToken(refreshToken);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals(newAccessToken, response.accessToken());
        assertEquals(newRefreshToken, response.refreshToken());

        // Verify
        verify(refreshTokenService).refreshAccessToken(refreshToken);
    }

    @Test
    @DisplayName("Should propagate exception when refresh token is invalid")
    void testRefreshAccessTokenInvalid() {
        // Arrange
        when(refreshTokenService.refreshAccessToken(refreshToken))
                .thenThrow(new InvalidCredentialsException("Refresh token has been revoked"));

        // Act & Assert
        assertThrows(
                InvalidCredentialsException.class,
                () -> authenticationService.refreshAccessToken(refreshToken)
        );

        verify(refreshTokenService).refreshAccessToken(refreshToken);
    }

    // ==================== INTEGRATION-LIKE TESTS ====================

    @Test
    @DisplayName("Should handle full authentication flow: authenticate -> verifyMfa -> refreshToken")
    void testFullAuthenticationFlowWithMfa() {
        // Step 1: Authenticate
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(testEmail, testPassword);
        Credential mfaCredential = new Credential();
        mfaCredential.setCredentialType(CredentialsType.TOTP);

        when(userManager.getUserByEmailAddress(testEmail)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.PASSWORD, testPassword))
                .thenReturn(true);
        when(credentialsManager.getActiveCredential(testUser, CredentialsType.TOTP))
                .thenReturn(Optional.of(mfaCredential));
        when(tokenService.generateMfaChallengeToken(testUser)).thenReturn(mfaChallengeToken);

        AuthenticationResponseDTO authResponse = authenticationService.authenticate(authRequest);
        assertTrue(authResponse.mfaChallengeToken() != null);

        // Step 2: Verify MFA
        String mfaCode = "123456";
        MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(mfaChallengeToken, mfaCode);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("type")).thenReturn("mfa_challenge");
        when(mockClaims.getSubject()).thenReturn(userId.toString());

        when(tokenService.extractClaims(mfaChallengeToken)).thenReturn(mockClaims);
        when(userManager.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(credentialsManager.validateCredential(testUser, CredentialsType.TOTP, mfaCode))
                .thenReturn(true);
        when(tokenService.generateToken(testUser)).thenReturn(accessToken);
        when(tokenService.generateRefreshToken(testUser)).thenReturn(refreshToken);

        MFAVerificationResponseDTO mfaResponse = authenticationService.verifyMfa(mfaRequest);
        assertTrue(mfaResponse.success());

        // Step 3: Refresh token
        String newAccessToken = "new.access.token";
        String newRefreshToken = "new.refresh.token";
        RefreshTokenService.RefreshTokenResult result = new RefreshTokenService.RefreshTokenResult(
                newAccessToken, newRefreshToken
        );
        when(refreshTokenService.refreshAccessToken(refreshToken)).thenReturn(result);

        AuthenticationResponseDTO refreshResponse = authenticationService.refreshAccessToken(refreshToken);
        assertTrue(refreshResponse.success());

        verify(refreshTokenService, times(2)).saveRefreshToken(any(), any());
    }
}

