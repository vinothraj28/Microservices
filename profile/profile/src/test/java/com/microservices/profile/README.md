# Authentication Service Unit Tests

This directory contains comprehensive unit tests for the authentication services in the Profile microservice. The test suite covers all critical authentication flows and security scenarios.

## Test Coverage

### 1. AuthenticationServiceImplTest
**File:** `services/Impl/AuthenticationServiceImplTest.java`

Comprehensive test suite for the main authentication orchestration service.

#### Test Categories

**Authentication Flow (7 tests)**
- `testAuthenticateSuccessfulWithoutMfa`: Verifies successful authentication when MFA is disabled
- `testAuthenticateSuccessfulWithMfaRequired`: Validates MFA challenge token generation when MFA is enabled
- `testAuthenticateUserNotFound`: Tests error handling for non-existent users
- `testAuthenticateInvalidPassword`: Verifies rejection of invalid passwords
- `testAuthenticateWithMfaRequired`: Tests MFA challenge flow
- Tests for both happy paths and error scenarios
- Validates that refresh tokens are only saved when MFA is not required

**MFA Verification (5 tests)**
- `testVerifyMfaSuccessful`: Confirms successful MFA code verification
- `testVerifyMfaInvalidTokenType`: Tests rejection of tokens with wrong type
- `testVerifyMfaExpiredToken`: Validates expiration checking
- `testVerifyMfaUserNotFound`: Tests handling of missing users
- `testVerifyMfaInvalidCode`: Verifies rejection of incorrect MFA codes
- Additional tests for malformed user IDs and state errors

**Logout (2 tests)**
- `testLogoutSuccessful`: Verifies token revocation on logout
- `testLogoutWithException`: Tests graceful failure handling

**Token Refresh (2 tests)**
- `testRefreshAccessTokenSuccessful`: Validates new token generation from valid refresh token
- `testRefreshAccessTokenInvalid`: Tests error handling for invalid tokens

**Integration Tests (1 test)**
- `testFullAuthenticationFlowWithMfa`: End-to-end test covering authenticate → verifyMfa → refresh flow

#### Key Testing Patterns
- Uses Mockito for dependency injection and verification
- Tests both success and failure paths
- Validates transactional behavior
- Checks database interactions (via mocks)
- Verifies error messages are generic (prevents user enumeration)

---

### 2. JWTServiceTest
**File:** `services/jwt/JWTServiceTest.java`

Comprehensive test suite for JWT token generation, validation, and claims extraction.

#### Test Categories

**Access Token Generation (4 tests)**
- `testGenerateTokenSuccess`: Validates proper token structure and claims
- `testGenerateTokenUniqueness`: Ensures unique JTI for each token
- `testGenerateTokenWithRoles`: Verifies role claim inclusion
- `testAccessTokenExpiration`: Validates correct expiration time calculation

**Refresh Token Generation (3 tests)**
- `testGenerateRefreshTokenSuccess`: Validates refresh token structure
- `testRefreshTokenExpiration`: Verifies extended expiration (7 days)
- `testRefreshTokenNoRoles`: Confirms roles are not included in refresh tokens

**MFA Challenge Tokens (2 tests)**
- `testGenerateMfaChallengeTokenSuccess`: Validates MFA token structure
- `testMfaChallengeTokenShortExpiry`: Verifies 5-minute expiration

**Token Validation (5 tests)**
- `testIsValidWithValidToken`: Tests validation of correctly signed tokens
- `testIsValidWithInvalidSignature`: Verifies rejection of tampered signatures
- `testIsValidWithMalformedToken`: Tests error handling for malformed tokens
- `testIsValidWithEmptyToken`: Tests edge case of empty token
- `testExtractClaimsFromInvalidToken`: Verifies exception handling

**Claims Extraction (4 tests)**
- `testExtractUserId`: Validates user ID extraction
- `testExtractJti`: Tests JTI (JWT ID) extraction
- `testExtractRoles`: Verifies role extraction
- `testExtractClaimsFromAccessToken`: Full claims validation for access tokens
- `testExtractClaimsFromRefreshToken`: Full claims validation for refresh tokens
- `testExtractClaimsFromMfaChallengeToken`: Full claims validation for MFA tokens

**Security Tests (4 tests)**
- `testRejectTokenWithDifferentIssuer`: Validates issuer verification
- `testRejectTokenWithDifferentAudience`: Tests audience verification
- `testPreserveTokenIntegrityMultipleGenerations`: Ensures consistent token generation
- `testTokenWithMultipleRoles`: Tests tokens with role information

#### Key Testing Patterns
- Uses ReflectionTestUtils to inject configuration
- Tests all token types (access, refresh, MFA challenge)
- Validates JWT structure (3-part format)
- Checks claim integrity
- Tests token signing/verification
- Validates expiration calculations

---

### 3. RefreshTokenServiceImplTest
**File:** `services/Impl/RefreshTokenServiceImplTest.java`

Complete test coverage for refresh token lifecycle management.

#### Test Categories

**Token Saving (3 tests)**
- `testSaveRefreshTokenSuccess`: Validates database storage
- `testSaveRefreshTokenInvalidFormat`: Tests error handling for malformed tokens
- `testSaveRefreshTokenExpirationCalculation`: Verifies correct expiration time storage

**Token Refresh Flow (9 tests)**
- `testRefreshAccessTokenSuccess`: Core refresh functionality
- `testRefreshAccessTokenInvalidType`: Validates token type checking
- `testRefreshAccessTokenMissingType`: Tests handling of missing type claim
- `testRefreshAccessTokenInvalidUserId`: Tests UUID validation
- `testRefreshAccessTokenNotFound`: Validates database lookup
- `testRefreshAccessTokenRevoked`: Tests rejection of revoked tokens
- `testRefreshAccessTokenExpired`: Verifies expiration checking
- `testRefreshAccessTokenUserNotFound`: Tests missing user scenarios
- `testRefreshAccessTokenJwtException`: Tests JWT parsing failures
- `testRefreshAccessTokenRevokesOldToken`: Validates old token revocation

**Token Revocation (3 tests)**
- `testRevokeRefreshTokenSuccess`: Validates revocation operation
- `testRevokeRefreshTokenJwtException`: Tests graceful failure handling
- `testRevokeAllTokensForUserSuccess`: Validates bulk revocation

**Integration Tests (1 test)**
- `testCompleteTokenLifecycle`: Tests full lifecycle: save → validate → refresh → revoke

#### Key Testing Patterns
- Tests database state transitions
- Validates token type and expiration checking
- Tests error handling with specific exception types
- Verifies old token revocation before issuing new ones
- Tests UUID validation and parsing
- Validates time-based token expiration

---

### 4. JWTFilterTest
**File:** `jwt/JWTFilterTest.java`

Comprehensive test suite for JWT servlet filter.

#### Test Categories

**No Authorization Header (3 tests)**
- `testNoAuthorizationHeader`: Tests handling of missing header
- `testBlankAuthorizationHeader`: Tests empty header handling
- `testNonBearerAuthorizationHeader`: Tests non-Bearer scheme rejection

**Valid Token Processing (3 tests)**
- `testValidTokenAuthentication`: Core authentication flow
- `testEmailExtraction`: Validates email claim extraction
- `testTokenExtraction`: Tests Bearer token parsing

**Invalid Token Handling (2 tests)**
- `testInvalidToken`: Tests rejection of invalid tokens
- `testExpiredToken`: Tests handling of expired tokens

**JWT Exception Handling (2 tests)**
- `testJwtExceptionDuringValidation`: Tests exception during token validation
- `testJwtExceptionDuringClaimsExtraction`: Tests exception during claims extraction

**Security Context Management (2 tests)**
- `testSecurityContextAuthentication`: Validates UsernamePasswordAuthenticationToken creation
- `testSecurityContextOnFailure`: Tests SecurityContext clearing on failure

**Edge Cases (3 tests)**
- `testBearerHeaderWithExtraSpaces`: Tests malformed Bearer header
- `testEmptyEmailInClaims`: Tests null/empty email handling
- `testNullEmailInClaims`: Tests null email in token

**Filter Chain Management (2 tests)**
- `testFilterChainContinuesAfterSuccess`: Validates chain continuation on success
- `testFilterChainStopsAfterFailure`: Tests chain termination on failure

#### Key Testing Patterns
- Tests servlet filter behavior
- Validates SecurityContext population
- Tests HTTP response codes (401 Unauthorized)
- Tests edge cases in header parsing
- Verifies filter chain behavior

---

## Running the Tests

### Run All Authentication Tests
```bash
cd profile/profile
mvn clean test -Dtest="*AuthenticationServiceImplTest,*JWTServiceTest,*RefreshTokenServiceImplTest,*JWTFilterTest"
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthenticationServiceImplTest
mvn test -Dtest=JWTServiceTest
mvn test -Dtest=RefreshTokenServiceImplTest
mvn test -Dtest=JWTFilterTest
```

### Run with Coverage Report
```bash
mvn clean test jacoco:report
# Report available at: target/site/jacoco/index.html
```

### Run Tests with Verbose Output
```bash
mvn test -Dtest=AuthenticationServiceImplTest -X
```

---

## Test Dependencies

The tests use the following key dependencies (already in pom.xml):

- **JUnit 5** (jupiter-api): Test framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Spring testing utilities
- **Spring Security Test**: Security testing utilities

### Maven Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Test Statistics

| Test Class | Test Count | Coverage Focus |
|------------|-----------|-----------------|
| AuthenticationServiceImplTest | 17 | Authentication flows, MFA, token refresh |
| JWTServiceTest | 24 | Token generation, validation, claims extraction |
| RefreshTokenServiceImplTest | 16 | Token lifecycle, refresh, revocation |
| JWTFilterTest | 19 | Servlet filtering, SecurityContext, exception handling |
| **TOTAL** | **76** | Complete authentication layer |

---

## Testing Strategy

### Isolation
- Each service is tested in isolation using Mockito mocks
- No database or external service calls
- Tests run quickly (typically < 100ms per test)

### Comprehensiveness
- All public methods are tested
- Both success and failure paths are covered
- Edge cases and error conditions are tested
- Security scenarios are explicitly tested

### Maintainability
- Tests use descriptive `@DisplayName` annotations
- Clear test organization with comments
- Consistent naming patterns (test{Method}{Scenario})
- Reusable setup and fixtures

### Best Practices
- One assertion concept per test (where possible)
- Arrange-Act-Assert pattern
- Meaningful error messages
- No test interdependencies
- Clear mocking strategy

---

## Key Test Scenarios

### Authentication Security
- ✅ User enumeration prevention (generic error messages)
- ✅ Password validation without storing plain text
- ✅ MFA challenge token time-limited (5 minutes)
- ✅ Token type verification (access vs. refresh vs. MFA)
- ✅ Refresh token revocation support

### Token Security
- ✅ Token signature validation
- ✅ Issuer and audience verification
- ✅ Expiration time checking
- ✅ JTI uniqueness (prevents replay attacks)
- ✅ Proper RBAC role inclusion

### Database State Management
- ✅ Token persistence for revocation
- ✅ Old token revocation before issuing new ones
- ✅ Refresh token expiration tracking
- ✅ User association validation

### Error Handling
- ✅ Invalid credentials
- ✅ Expired tokens
- ✅ Malformed JWTs
- ✅ Missing users
- ✅ Revoked tokens

---

## Extending the Tests

### Adding Tests for New Authentication Features

1. **MFA Setup Tests**: When adding MFA enrollment endpoint
   - Test TOTP secret generation
   - Test QR code generation
   - Test MFA credential activation

2. **Password Reset Tests**: When adding password reset flow
   - Test reset token generation and validation
   - Test password update with old token revocation

3. **Session Management Tests**: When adding session tracking
   - Test concurrent session limits
   - Test device tracking

### Test Template

```java
@Test
@DisplayName("Description of what is being tested")
void testFeatureName() {
    // Arrange
    setupTestData();
    when(dependency.method()).thenReturn(value);
    
    // Act
    var result = serviceUnderTest.methodName();
    
    // Assert
    assertEquals(expected, actual);
    verify(dependency).method();
}
```

---

## Continuous Integration

These tests are designed to run in CI/CD pipelines:

```yaml
# Example GitHub Actions configuration
- name: Run Tests
  run: mvn clean test -Dtest="*Test"
  
- name: Generate Coverage
  run: mvn jacoco:report
```

---

## Notes for Developers

### When to Run Tests
- Before committing code
- After modifying authentication services
- Before deployment
- When debugging authentication issues

### Common Test Issues and Solutions

| Issue | Solution |
|-------|----------|
| Tests fail with JWT signature errors | Ensure JWT secret matches across all mocks |
| Timezone-related assertion failures | Use UTC for all timestamp tests |
| SecurityContext not cleared | Use `@BeforeEach void setUp()` to reset context |
| Flaky expiration tests | Allow 5-second tolerance for time-based calculations |

---

## Related Documentation

- **AGENTS.md**: Architecture overview and authentication patterns
- **AuthenticationService.java**: Service interface documentation
- **JWTService.java**: JWT implementation details
- **RefreshTokenService.java**: Token lifecycle management

---

## Test Maintenance

### Regular Tasks
- Review test coverage quarterly
- Update tests when authentication flows change
- Add tests for security vulnerabilities discovered
- Keep test data realistic and up-to-date

### Code Review Checklist
- ✅ All new public methods have tests
- ✅ Error paths are tested
- ✅ Security scenarios are covered
- ✅ Tests use proper naming and comments
- ✅ No hardcoded values (use constants)
- ✅ Mock objects are properly verified

---

**Last Updated:** July 15, 2026  
**Test Suite Version:** 1.0  
**Total Test Cases:** 76

