# Quick Start: Running Authentication Tests

## Quick Commands

### Run All Authentication Tests
```bash
cd D:\Microservices\profile\profile
mvn clean test -Dtest="*AuthenticationServiceImplTest,*JWTServiceTest,*RefreshTokenServiceImplTest,*JWTFilterTest"
```

### Run Individual Test Classes
```bash
# Authentication Service Tests
mvn test -Dtest=AuthenticationServiceImplTest

# JWT Token Generation/Validation Tests  
mvn test -Dtest=JWTServiceTest

# Refresh Token Lifecycle Tests
mvn test -Dtest=RefreshTokenServiceImplTest

# JWT Servlet Filter Tests
mvn test -Dtest=JWTFilterTest
```

### Run All Profile Service Tests
```bash
mvn clean test
```

## What Each Test Class Tests

### 1. AuthenticationServiceImplTest (17 tests)
Tests the main authentication orchestration service:
- User authentication with and without MFA
- MFA verification flow
- Logout functionality
- Access token refresh
- Error handling (invalid credentials, expired tokens, missing users)

**Run:**
```bash
mvn test -Dtest=AuthenticationServiceImplTest
```

### 2. JWTServiceTest (24 tests)
Tests JWT token generation, validation, and claims extraction:
- Access token generation and structure
- Refresh token generation
- MFA challenge token generation
- Token validation and signature verification
- Claims extraction (userId, email, roles, etc.)
- Security validation (issuer, audience verification)

**Run:**
```bash
mvn test -Dtest=JWTServiceTest
```

### 3. RefreshTokenServiceImplTest (16 tests)
Tests refresh token lifecycle management:
- Token saving and database storage
- Access token refresh flow
- Refresh token revocation
- Bulk user token revocation
- Token expiration and database state validation

**Run:**
```bash
mvn test -Dtest=RefreshTokenServiceImplTest
```

### 4. JWTFilterTest (19 tests)
Tests JWT servlet filter for HTTP requests:
- Authorization header parsing
- Token validation
- SecurityContext population
- Error handling (missing tokens, invalid signatures)
- Filter chain behavior

**Run:**
```bash
mvn test -Dtest=JWTFilterTest
```

## Test Execution Examples

### Run Single Test Method
```bash
mvn test -Dtest=AuthenticationServiceImplTest#testAuthenticateSuccessfulWithoutMfa
```

### Run with Verbose Output
```bash
mvn test -Dtest=AuthenticationServiceImplTest -X
```

### Run with Debug Logging
```bash
mvn test -Dtest=AuthenticationServiceImplTest -DargLine="-Dlogging.level.root=DEBUG"
```

### View Test Reports
After running tests, view the report:
```bash
# HTML Report
target/surefire-report.html

# Text Output
target/surefire-reports/
```

## Prerequisites

Ensure you have:
- JDK 21 or higher
- Maven 3.8.1 or higher
- All dependencies downloaded (run `mvn dependency:download-sources` first)

## Expected Test Results

✅ **All tests should pass** (76 total tests)

Example successful output:
```
Tests run: 76, Failures: 0, Errors: 0, Skipped: 0
```

## Troubleshooting

### Tests Won't Compile
**Problem:** "Cannot find symbol" errors
**Solution:**
```bash
mvn clean install -DskipTests
mvn test -Dtest=AuthenticationServiceImplTest
```

### Tests Timeout
**Problem:** Tests take too long or hang
**Solution:** Increase timeout
```bash
mvn test -Dtest=AuthenticationServiceImplTest -DtestFailureIgnore=true
```

### JWT Tests Fail (Invalid Signature)
**Problem:** JWT signature validation fails
**Solution:** Ensure JWT secret is properly base64-encoded in test setup
- Check ReflectionTestUtils configuration in test
- Verify secret matches in all services

### SecurityContext Errors
**Problem:** SecurityContextHolder.getContext() is null
**Solution:** Add `@BeforeEach` to clear context
```java
@BeforeEach
void setUp() {
    SecurityContextHolder.clearContext();
    // ... other setup
}
```

## IDE Integration

### IntelliJ IDEA
1. Right-click on test class → "Run Tests"
2. Right-click on `src/test/java` → "Run Tests in..."
3. Or use Ctrl+Shift+F10 on test file

### Eclipse
1. Right-click test class → "Run As" → "JUnit Test"
2. Or use Alt+Shift+X, T

### VS Code
1. Install "Test Runner for Java" extension
2. Click "Run Test" above test class names

## Continuous Integration

Add to your CI/CD pipeline (GitHub Actions example):

```yaml
name: Test Authentication Services

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: 21
      
      - name: Run Tests
        run: |
          cd profile/profile
          mvn clean test
      
      - name: Upload Coverage
        uses: codecov/codecov-action@v2
        with:
          files: ./profile/profile/target/site/jacoco/jacoco.xml
```

## Code Coverage

Generate test coverage report:

```bash
mvn clean test jacoco:report
# View: target/site/jacoco/index.html
```

Expected coverage for authentication services:
- **AuthenticationService**: >95%
- **JWTService**: >95%
- **RefreshTokenService**: >90%
- **JWTFilter**: >90%

## Test Data

Tests use realistic test data:
- Email: john.doe@example.com
- Passwords: TestPassword123!
- UserID: Random UUID
- Tokens: Valid JWT format with proper claims

## Next Steps

1. ✅ Run the tests locally: `mvn clean test`
2. ✅ Check test reports in `target/surefire-reports/`
3. ✅ Review test code for examples of testing patterns
4. ✅ Add new tests when adding authentication features
5. ✅ Keep tests passing in CI/CD pipeline

## Additional Resources

- **Test Documentation:** See `README.md` in this directory
- **AGENTS.md:** Architecture and authentication patterns
- **Spring Boot Testing Guide:** https://spring.io/guides/gs/testing-web/
- **Mockito Documentation:** https://javadoc.io/doc/org.mockito/mockito-core/

## Common Test Patterns Used

### 1. Mocking Dependencies
```java
@Mock
private UserManager userManager;

@InjectMocks
private AuthenticationServiceImpl authService;
```

### 2. Arrange-Act-Assert
```java
void testFeature() {
    // Arrange
    when(mock.method()).thenReturn(value);
    
    // Act
    result = service.methodUnderTest();
    
    // Assert
    assertEquals(expected, result);
}
```

### 3. Verification
```java
// Verify method was called
verify(mock).method(argument);

// Verify method was never called
verify(mock, never()).method();

// Verify call count
verify(mock, times(2)).method();
```

---

**Last Updated:** July 15, 2026
**Test Suite Version:** 1.0

