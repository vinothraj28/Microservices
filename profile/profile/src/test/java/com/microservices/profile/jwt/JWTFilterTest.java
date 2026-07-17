package com.microservices.profile.jwt;

import com.microservices.profile.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWTFilter Tests")
class JWTFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTFilter jwtFilter;

    private String validToken;
    private String testEmail;

    @BeforeEach
    void setUp() {
        validToken = "valid.jwt.token";
        testEmail = "john.doe@example.com";
        SecurityContextHolder.clearContext();
    }

    // ==================== NO AUTHORIZATION HEADER TESTS ====================

    @Test
    @DisplayName("Should proceed without authentication when Authorization header is missing")
    void testNoAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(tokenService, never()).isValid(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should proceed without authentication when Authorization header is blank")
    void testBlankAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("");

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(tokenService, never()).isValid(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should proceed without authentication when Authorization header doesn't start with Bearer")
    void testNonBearerAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic dGVzdDp0ZXN0");

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(tokenService, never()).isValid(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    // ==================== VALID TOKEN TESTS ====================

    @Test
    @DisplayName("Should authenticate user with valid JWT token")
    void testValidTokenAuthentication() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(testEmail);
        when(tokenService.extractClaims(validToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenService).isValid(validToken);
        verify(tokenService).extractClaims(validToken);
        verify(filterChain).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(testEmail, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Should extract email from JWT claims")
    void testEmailExtraction() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        String customEmail = "custom.email@example.com";

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(customEmail);
        when(tokenService.extractClaims(validToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(customEmail, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Should correctly extract token from Bearer header")
    void testTokenExtraction() throws ServletException, IOException {
        // Arrange
        String bearerToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMTIzIn0.signature";
        String authHeader = "Bearer " + bearerToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(bearerToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(testEmail);
        when(tokenService.extractClaims(bearerToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenService).isValid(bearerToken);
        verify(tokenService).extractClaims(bearerToken);
    }

    // ==================== INVALID TOKEN TESTS ====================

    @Test
    @DisplayName("Should reject invalid or expired token")
    void testInvalidToken() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should reject expired token")
    void testExpiredToken() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(tokenService, never()).extractClaims(validToken);
    }

    // ==================== JWT EXCEPTION TESTS ====================

    @Test
    @DisplayName("Should handle JWT validation exception")
    void testJwtExceptionDuringValidation() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenThrow(new JwtException("Invalid signature"));

        // Act & Assert
        assertDoesNotThrow(() -> jwtFilter.doFilterInternal(request, response, filterChain));

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle JWT exception during claims extraction")
    void testJwtExceptionDuringClaimsExtraction() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);
        when(tokenService.extractClaims(validToken)).thenThrow(new JwtException("Invalid token"));

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    // ==================== AUTHENTICATION CONTEXT TESTS ====================

    @Test
    @DisplayName("Should set UsernamePasswordAuthenticationToken in SecurityContext")
    void testSecurityContextAuthentication() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(testEmail);
        when(tokenService.extractClaims(validToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(testEmail, auth.getPrincipal());
        assertNull(auth.getCredentials());
        assertTrue(auth.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("Should clear SecurityContext on token validation failure")
    void testSecurityContextOnFailure() throws ServletException, IOException {
        // Arrange - First set authentication
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Should handle Bearer header with extra spaces")
    void testBearerHeaderWithExtraSpaces() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer  " + validToken; // Extra space
        String extractedToken = " " + validToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(extractedToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(testEmail);
        when(tokenService.extractClaims(extractedToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenService).isValid(extractedToken);
    }

    @Test
    @DisplayName("Should handle empty email in claims")
    void testEmptyEmailInClaims() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn("");
        when(tokenService.extractClaims(validToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals("", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Should handle null email in claims")
    void testNullEmailInClaims() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(null);
        when(tokenService.extractClaims(validToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    // ==================== CHAINING TESTS ====================

    @Test
    @DisplayName("Should continue filter chain after successful authentication")
    void testFilterChainContinuesAfterSuccess() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(true);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("email", String.class)).thenReturn(testEmail);
        when(tokenService.extractClaims(validToken)).thenReturn(mockClaims);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should not continue filter chain after authentication failure")
    void testFilterChainStopsAfterFailure() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenService.isValid(validToken)).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, never()).doFilter(request, response);
    }
}

