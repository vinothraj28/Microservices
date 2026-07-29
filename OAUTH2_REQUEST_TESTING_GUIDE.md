# OAuth2 Request Testing Guide (Phase 3 - Session-Based Auth)

This guide covers how to test the OAuth2 work completed through **Phase 5** (Profile gRPC + Gateway OAuth2 endpoints with session-based authentication).

## 1. What is implemented now

Phase 3 added:

- OAuth2 repositories:
  - `OAuth2ClientRepository`
  - `AuthorizationCodeRepository`
  - `RevokedTokenRepository`
- OAuth2 services:
  - `OAuth2ClientService`
  - `OAuth2AuthorizationCodeService`
  - `OAuth2TokenService`
  - `PKCEValidator`
- OAuth2 config:
  - `oauth2.authorization-code-ttl-seconds`
  - `jwt.refresh-expiration-days`
- **Session Management (NEW)**:
  - `OAuth2SessionService` - Manages user sessions for OAuth2 flow
  - `SessionConfig` - Redis-backed session configuration
  - Session establishment endpoint for Angular frontend integration

Gateway OAuth2 endpoints are now exposed and wired to Profile gRPC OAuth2 service.

## 2. Session-Based Authorization Flow

**IMPORTANT**: The `/authorize` endpoint now requires an active session. The flow works as follows:

### Flow Diagram

```
1. Third-party App → GET /authorize?client_id=...&redirect_uri=...
                     ↓
2. Gateway checks session → NO SESSION
                     ↓
3. Gateway → 302 Redirect to http://localhost:4200/login?returnUrl=...
                     ↓
4. User logs in on Angular frontend
                     ↓
5. Angular → POST /api/v1/auth/authenticate → receives JWT access token
                     ↓
6. Angular → POST /api/v1/oauth2/session/establish (with JWT)
                     ↓
7. Gateway creates session, returns redirect URL
                     ↓
8. Angular redirects user back to /authorize with session cookie
                     ↓
9. Gateway checks session → SESSION EXISTS → issues authorization code
                     ↓
10. Gateway → 302 Redirect to client redirect_uri with code
```

### Prerequisites

1. **Redis must be running** on localhost:6379 (for session storage)
   ```bash
   # Windows: Start Redis server
   redis-server
   ```

2. **PostgreSQL must be running** on localhost:5432

3. **Profile Service must be running** on port 8081 (gRPC on 9090)
   ```bash
   cd profile/profile
   mvn spring-boot:run
   ```

4. **Gateway must be running** on port 8080
   ```bash
   cd gateway/gateway
   mvn spring-boot:run
   ```

5. **Angular Frontend** (assumed running on localhost:4200)
   - Must have login page at `/login`
   - Must handle `returnUrl` query parameter
   - Must call `/api/v1/oauth2/session/establish` after login

## 2. Run automated tests

From `d:\Microservices\profile\profile`:

```bash
mvn -Dtest=PKCEValidatorImplTest test
```

Run all Profile tests:

```bash
mvn test
```

## 3. Manual database validation checklist

After service calls (or integration tests), confirm tables:

- `oauth2_clients` has registered client row
- `authorization_codes` row created with:
  - `used = false` initially
  - `expires_at` near `now + 600s`
- `refresh_tokens` row created with:
  - `client_id` populated
  - `scope` populated
  - `parent_token_id` null on first issue, populated after refresh
- `revoked_tokens` row created after access-token revocation

## 4. Request test payloads for Gateway OAuth2 endpoints (Phase 5)

Gateway endpoints:
- `POST /api/v1/oauth2/register` - Register OAuth2 client
- `POST /api/v1/oauth2/session/establish` - Establish session after login (NEW)
- `GET /api/v1/oauth2/authorize` - Authorization endpoint (requires session)
- `POST /api/v1/oauth2/token` - Exchange code for tokens
- `POST /api/v1/oauth2/introspect` - Introspect token
- `POST /api/v1/oauth2/revoke` - Revoke token
- `POST /api/v1/oauth2/session/logout` - Logout session (NEW)

### 4.1 Register client

`POST /api/v1/oauth2/register`

```json
{
  "clientName": "IAM Angular SPA",
  "redirectUris": ["http://localhost:4200/callback"],
  "scopes": ["user:read", "user:write"],
  "clientType": "PUBLIC",
  "grantTypes": ["AUTHORIZATION_CODE", "REFRESH_TOKEN"],
  "applicationType": "spa"
}
```

Expected:

- `client_id` returned
- `client_secret` empty/null for PUBLIC client

### 4.2 User Login & Session Establishment (NEW)

**Step 1**: User registers/logs in via Angular frontend

`POST /api/v1/auth/authenticate`

```json
{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

Expected response:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "mfaRequired": false
}
```

**Step 2**: Angular establishes OAuth2 session

`POST /api/v1/oauth2/session/establish`

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9..."
}
```

Expected response:
```json
{
  "success": true,
  "sessionId": "12345-abcde-67890",
  "redirectUrl": "http://localhost:8080/api/v1/oauth2/authorize?client_id=..."
}
```

**Important**: The session cookie `IAM_SESSION` is set in the response. This cookie must be included in subsequent requests to `/authorize`.

### 4.3 Authorize (PKCE) - NOW REQUIRES SESSION

`GET /api/v1/oauth2/authorize` with query params:

- `response_type=code`
- `client_id=<client_id>`
- `redirect_uri=http://localhost:4200/callback`
- `scope=user:read user:write`
- `state=<random_state>`
- `code_challenge=<pkce_s256_challenge>`
- `code_challenge_method=S256`

**IMPORTANT CHANGES**:
- ❌ No longer accepts `user_id` query parameter
- ❌ No longer accepts `Authorization: Bearer` header
- ✅ Requires `IAM_SESSION` cookie (set after session establishment)
- ✅ Redirects to Angular login if session not found

**Without Session**:
- Returns: `302 Found`
- Location: `http://localhost:4200/login?returnUrl=http://localhost:8080/api/v1/oauth2/authorize?...`

**With Valid Session**:
- Returns: `302 Found`
- Location: `http://localhost:4200/callback?code=<authorization_code>&state=<state>`

### 4.4 Exchange code for tokens

`POST /api/v1/oauth2/token`

```json
{
  "grantType": "authorization_code",
  "code": "<authorization_code>",
  "redirectUri": "http://localhost:4200/callback",
  "clientId": "<client_id>",
  "codeVerifier": "<pkce_code_verifier>"
}
```

Expected:

- `access_token`, `refresh_token`, `token_type=Bearer`, `expires_in`
- DB: `authorization_codes.used = true`
- DB: one row in `refresh_tokens`

### 4.5 Refresh token

`POST /api/v1/oauth2/token`

```json
{
  "grantType": "refresh_token",
  "refreshToken": "<refresh_token>",
  "clientId": "<client_id>"
}
```

Expected:

- new `access_token` and new `refresh_token`
- old refresh token revoked
- new row has `parent_token_id` = old token ID

### 4.6 Revoke token

`POST /api/v1/oauth2/revoke`

```json
{
  "token": "<access_or_refresh_token>",
  "tokenTypeHint": "access_token",
  "clientId": "<client_id>"
}
```

Expected:

- refresh token: `refresh_tokens.revoked = true`
- access token: row inserted in `revoked_tokens`

## 5. PKCE test vectors

Reference verifier/challenge pair:

- code_verifier: `dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk`
- S256 code_challenge: `E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM`

Use this pair to confirm `PKCEValidator` behavior.

## 6. Negative test cases (must fail)

1. Invalid client_id on authorize/token
2. Redirect URI mismatch
3. Expired authorization code
4. Reused authorization code
5. Wrong code_verifier
6. Refresh token used with another client_id
7. Requested refresh scope exceeds original scope
8. Revoked refresh token reuse

These failures should return validation errors and must not mint new tokens.

## 7. End-to-end curl sequence (with session management)

### Prerequisites
```bash
# Start Redis
redis-server

# Register a user first (if not already registered)
curl -X POST http://localhost:8080/api/v1/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"userName\":\"testuser\",\"emailAddress\":\"test@example.com\",\"password\":\"SecurePass123\",\"dob\":\"1990-01-01\"}"
```

### Full OAuth2 Flow

**1. Register OAuth2 client**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/register ^
  -H "Content-Type: application/json" ^
  -d "{\"clientName\":\"IAM Angular SPA\",\"redirectUris\":[\"http://localhost:4200/callback\"],\"scopes\":[\"user:read\",\"user:write\"],\"clientType\":\"PUBLIC\",\"grantTypes\":[\"AUTHORIZATION_CODE\",\"REFRESH_TOKEN\"],\"applicationType\":\"spa\"}"
```

Save the `client_id` from response.

**2. Simulate user login (get JWT)**
```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"SecurePass123\"}" ^
  -c cookies.txt
```

Save the `accessToken` from response.

**3. Establish OAuth2 session**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/session/establish ^
  -H "Content-Type: application/json" ^
  -d "{\"accessToken\":\"<ACCESS_TOKEN>\"}" ^
  -c cookies.txt ^
  -b cookies.txt
```

This creates a session and saves the `IAM_SESSION` cookie in `cookies.txt`.

**4. Authorize (will now succeed with session)**
```bash
curl -v "http://localhost:8080/api/v1/oauth2/authorize?response_type=code&client_id=<CLIENT_ID>&redirect_uri=http://localhost:4200/callback&scope=user:read%20user:write&state=abc123&code_challenge=E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM&code_challenge_method=S256" ^
  -b cookies.txt ^
  -L
```

**Important**: 
- Use `-v` to see redirect headers
- Use `-L` to follow redirects
- The session cookie from step 3 is sent via `-b cookies.txt`
- Extract `code` from the redirect Location header

**5. Exchange code for tokens**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/token ^
  -H "Content-Type: application/json" ^
  -d "{\"grantType\":\"authorization_code\",\"code\":\"<AUTH_CODE>\",\"redirectUri\":\"http://localhost:4200/callback\",\"codeVerifier\":\"dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk\",\"clientId\":\"<CLIENT_ID>\"}"
```

**6. Refresh token**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/token ^
  -H "Content-Type: application/json" ^
  -d "{\"grantType\":\"refresh_token\",\"refreshToken\":\"<REFRESH_TOKEN>\",\"clientId\":\"<CLIENT_ID>\"}"
```

**7. Introspect token**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/introspect ^
  -H "Content-Type: application/json" ^
  -d "{\"token\":\"<ACCESS_TOKEN>\",\"clientId\":\"<CLIENT_ID>\"}"
```

**8. Revoke token**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/revoke ^
  -H "Content-Type: application/json" ^
  -d "{\"token\":\"<ACCESS_TOKEN>\",\"tokenTypeHint\":\"access_token\",\"clientId\":\"<CLIENT_ID>\"}"
```

**9. Logout session**
```bash
curl -X POST http://localhost:8080/api/v1/oauth2/session/logout ^
  -b cookies.txt ^
  -c cookies.txt
```

## 8. Angular Frontend Integration

Your Angular frontend (localhost:4200) needs to implement:

### 8.1 Login Page (`/login`)

```typescript
// After successful login
loginUser(email: string, password: string) {
  this.http.post('/api/v1/auth/authenticate', { email, password })
    .subscribe(response => {
      const accessToken = response.accessToken;
      
      // Establish OAuth2 session
      this.http.post('/api/v1/oauth2/session/establish', { accessToken })
        .subscribe(sessionResponse => {
          // Session cookie is automatically set by browser
          
          // Check if user was redirected from OAuth2 authorize
          if (sessionResponse.redirectUrl) {
            // Redirect back to authorize endpoint
            window.location.href = sessionResponse.redirectUrl;
          } else {
            // Normal login, go to dashboard
            this.router.navigate(['/dashboard']);
          }
        });
    });
}
```

### 8.2 Handle `returnUrl` Query Parameter

```typescript
ngOnInit() {
  this.route.queryParams.subscribe(params => {
    this.returnUrl = params['returnUrl'] || null;
  });
}

// Pass returnUrl context when establishing session
```

### 8.3 OAuth Callback Page (`/callback`)

```typescript
// Extract authorization code from URL
ngOnInit() {
  this.route.queryParams.subscribe(params => {
    const code = params['code'];
    const state = params['state'];
    
    // Send code back to your application logic
    // or display for testing
  });
}
```

## 9. Testing Without Angular Frontend

If you don't have an Angular frontend yet, you can test manually with curl by:

1. **Manually creating a session**:
   - Use the session establishment endpoint with a valid JWT
   - Save cookies to a file (`-c cookies.txt`)

2. **Using cookies in subsequent requests**:
   - Pass cookies to authorize endpoint (`-b cookies.txt`)

3. **Following redirects**:
   - Use `curl -L` to follow redirects automatically
   - Or use `-v` to see redirect Location headers

Example test without frontend:
```bash
# 1. Login
ACCESS_TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"SecurePass123"}' \
  | jq -r '.accessToken')

# 2. Establish session
curl -X POST http://localhost:8080/api/v1/oauth2/session/establish \
  -H "Content-Type: application/json" \
  -d "{\"accessToken\":\"$ACCESS_TOKEN\"}" \
  -c session_cookies.txt

# 3. Authorize with session
curl -v "http://localhost:8080/api/v1/oauth2/authorize?..." \
  -b session_cookies.txt \
  -L
```
