# OAuth2 Session-Based Authentication Implementation

## Problem
The OAuth2 `/authorize` endpoint was incorrectly accepting `user_id` as a query parameter or JWT token in the Authorization header. This is not the standard OAuth2 flow. The server should:
1. Check for an existing user session
2. Redirect to login page if no session exists
3. Issue authorization code only after user is authenticated via session

## Solution Implemented

### 1. **Session Management Layer**
- Added Spring Session with Redis backend for distributed session storage
- Created `OAuth2SessionService` to manage user sessions
- Session stores: `user_id`, `email`, and OAuth flow context (return URL)
- Session timeout: 30 minutes

### 2. **Session Configuration**
- `SessionConfig.java`: Configures Redis-backed WebSession
- Custom cookie name: `IAM_SESSION`
- Cookie settings: HttpOnly, SameSite=Lax (for OAuth redirects)

### 3. **Updated OAuth2 Flow**

#### Old Flow (INCORRECT):
```
Client → /authorize?user_id=X → Authorization Code
```

#### New Flow (CORRECT):
```
1. Client → /authorize → Check Session
2. No Session → 302 Redirect to Angular Login (http://localhost:4200/login)
3. User logs in → Angular gets JWT
4. Angular → POST /session/establish (with JWT)
5. Backend creates session → Returns redirect URL
6. Angular → Redirect back to /authorize WITH session cookie
7. Backend checks session → Session exists → Issue authorization code
8. 302 Redirect to client's redirect_uri with code
```

### 4. **New Endpoints**

#### `POST /api/v1/oauth2/session/establish`
- Called by Angular frontend after successful login
- Accepts JWT access token
- Extracts user_id and email from JWT
- Creates server-side session
- Returns session ID and redirect URL (if user came from /authorize)

#### `POST /api/v1/oauth2/session/logout`
- Invalidates the OAuth2 session
- Clears session from Redis

### 5. **Updated `/authorize` Endpoint**
- **Removed**: `user_id` query parameter
- **Removed**: `Authorization` header support
- **Added**: Session check via `OAuth2SessionService`
- **Added**: Automatic redirect to Angular login if no session
- **Added**: Stores OAuth request URL in session for post-login redirect
- **Changed**: Returns `302 Redirect` instead of JSON response

### 6. **Configuration**
Added to `application.yml`:
```yaml
spring:
  session:
    store-type: redis
    timeout: 30m

oauth2:
  frontend:
    base-url: http://localhost:4200
    login-path: /login
```

### 7. **Dependencies Added**
```xml
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

## Files Created
1. `OAuth2SessionService.java` - Session management logic
2. `SessionConfig.java` - Spring Session configuration
3. `SessionEstablishRequestDTO.java` - Request DTO for session establishment
4. `SessionEstablishResponseDTO.java` - Response DTO for session establishment

## Files Modified
1. `OAuth2Controller.java` - Updated authorize endpoint, added session endpoints
2. `SecurityConfig.java` - Allowed new session endpoints
3. `application.yml` - Added session and frontend configuration
4. `pom.xml` - Added Spring Session dependency
5. `OAUTH2_REQUEST_TESTING_GUIDE.md` - Complete rewrite with session flow

## Integration with Angular Frontend

The Angular frontend (localhost:4200) must implement:

### Login Flow
1. User lands on `/login?returnUrl=http://localhost:8080/api/v1/oauth2/authorize?...`
2. User enters credentials
3. Angular calls `/api/v1/auth/authenticate` → receives JWT
4. Angular calls `/api/v1/oauth2/session/establish` with JWT
5. Backend creates session, returns `redirectUrl`
6. Angular redirects user to `redirectUrl` (back to /authorize)
7. Browser automatically sends `IAM_SESSION` cookie
8. Backend issues authorization code

### Key Angular Implementation Points
- Store `returnUrl` from query parameters
- Call session establishment after login
- Redirect to `redirectUrl` from session establishment response
- Browser handles cookie storage automatically

## Testing

### Prerequisites
1. Redis running on localhost:6379
2. PostgreSQL running on localhost:5432
3. Profile service running on 8081/9090
4. Gateway running on 8080
5. Angular frontend on 4200 (optional for manual testing)

### Manual Testing with curl
See updated `OAUTH2_REQUEST_TESTING_GUIDE.md` section 7 for complete curl sequences.

Key points:
- Use `-c cookies.txt` to save cookies
- Use `-b cookies.txt` to send cookies
- Use `-v` to see redirect headers
- Use `-L` to follow redirects

## Security Considerations

### Session Security
- **HttpOnly**: Prevents JavaScript access to session cookie
- **SameSite=Lax**: Allows cross-origin for OAuth redirects while preventing CSRF
- **Secure**: Should be `true` in production (requires HTTPS)
- **Session Timeout**: 30 minutes idle timeout

### Redis Security
- Session data stored in Redis with key: `spring:session:sessions:<session-id>`
- Contains user_id and email (no password or sensitive data)
- Redis should be secured in production (authentication, network isolation)

## Why This Approach?

### Standards Compliance
- OAuth2 RFC 6749 requires user authentication before authorization
- Session-based auth is the standard pattern for web applications
- Separates concerns: authentication (JWT) vs authorization (OAuth2)

### User Experience
- Users authenticate once, session persists across OAuth flows
- Natural integration with Angular SPA architecture
- Server remains stateless for business logic (JWT), stateful for OAuth flow (session)

### Security
- No user_id in URL (prevents parameter tampering)
- No Bearer token in URL (prevents token leakage in logs)
- Session cookie is HttpOnly (prevents XSS token theft)
- CSRF protection via SameSite cookie attribute

## Future Enhancements
1. **Consent Screen**: Add consent page between authentication and authorization
2. **Remember Me**: Optional long-lived session for trusted devices
3. **Session Monitoring**: Admin dashboard for active sessions
4. **Multi-Factor Auth**: Enforce MFA before OAuth authorization
5. **Session Revocation**: Admin ability to revoke sessions
6. **Rate Limiting**: Per-session rate limits on authorization attempts

## Troubleshooting

### Common Issues

**Issue**: `/authorize` keeps redirecting to login
- **Cause**: Session cookie not being sent
- **Fix**: Check cookies.txt file, ensure `-b cookies.txt` in curl
- **Fix**: In browser, check Application → Cookies → IAM_SESSION

**Issue**: "Invalid access token" on session establishment
- **Cause**: JWT expired or invalid
- **Fix**: Login again to get fresh JWT
- **Fix**: Check JWT expiration time

**Issue**: Redis connection refused
- **Cause**: Redis not running
- **Fix**: Start Redis with `redis-server`

**Issue**: Session expired after redirect
- **Cause**: Session timeout too short
- **Fix**: Increase timeout in application.yml

## Migration Notes

### Breaking Changes
- `/authorize` endpoint no longer accepts `user_id` parameter
- `/authorize` endpoint no longer accepts `Authorization` header
- `/authorize` now returns 302 redirects instead of JSON

### Backward Compatibility
If you need backward compatibility for testing:
1. Keep old session establishment logic in a separate endpoint
2. Add a feature flag to enable/disable session checks
3. Gradually migrate clients to new flow

However, for production OAuth2 compliance, the session-based flow is mandatory.
