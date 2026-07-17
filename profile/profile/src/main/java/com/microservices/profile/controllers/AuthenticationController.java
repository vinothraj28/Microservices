package com.microservices.profile.controllers;

import com.microservices.profile.dto.auth.AuthenticationRequestDTO;
import com.microservices.profile.dto.auth.AuthenticationResponseDTO;
import com.microservices.profile.dto.auth.MFAVerificationRequestDTO;
import com.microservices.profile.dto.auth.MFAVerificationResponseDTO;
import com.microservices.profile.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Authentication Flows.
 *
 * Exposes two endpoints for the complete authentication lifecycle:
 * 1. POST /authenticate - Initial credential validation and MFA check
 * 2. POST /verify-mfa - MFA code verification and token issuance
 *
 * ARCHITECTURE:
 * - Handles HTTP concerns (request/response, status codes, headers)
 * - Delegates all business logic to AuthenticationService
 * - Uses DTOs for validation and serialization
 * - Returns standardized responses with appropriate HTTP status codes
 *
 * ERROR HANDLING:
 * - Validation errors: 422 Unprocessable Entity (from @Valid)
 * - Invalid credentials: 401 Unauthorized
 * - Invalid MFA challenge: 401 Unauthorized
 * - Other errors: Handled by GlobalExceptionController
 *
 * SECURITY:
 * - Both endpoints are unprotected (no JWT required for initial auth)
 * - MFA verification requires valid challenge token (issued by authenticate endpoint)
 * - Detailed error messages withheld to prevent user enumeration
 *
 * AUDIT:
 * - All authentication attempts should be logged (see AuthenticationService for details)
 * - Security events (failed attempts, MFA verification) should trigger alerting
 *
 * @see com.microservices.profile.services.AuthenticationService for business logic
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth/")
@Tag(name = "Authentication APIs", description = "User authentication and MFA verification endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Step 1: Authenticate user with email and password.
     *
     * This endpoint initiates the authentication process:
     * - Validates email and password
     * - Checks if MFA is enabled for the user
     * - Returns either:
     *   a) Access token (if MFA disabled) - user is authenticated
     *   b) MFA challenge token (if MFA enabled) - user must verify MFA
     *
     * REQUEST BODY:
     * {
     *   "email": "user@example.com",
     *   "password": "SecurePassword123"
     * }
     *
     * RESPONSE (MFA Not Required):
     * HTTP 200 OK
     * {
     *   "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
     *   "mfaChallengeToken": null,
     *   "mfaRequired": false
     * }
     *
     * RESPONSE (MFA Required):
     * HTTP 200 OK
     * {
     *   "accessToken": null,
     *   "mfaChallengeToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
     *   "mfaRequired": true
     * }
     *
     * ERROR RESPONSES:
     * - 401 Unauthorized: Invalid email or password
     * - 422 Unprocessable Entity: Validation failed (invalid email format, blank fields)
     *
     * @param authRequest Email and password credentials
     * @return ResponseEntity with AuthenticationResponseDTO
     */
    @Operation(
            summary = "Authenticate user with email and password",
            description = "Initiates authentication process. Returns JWT token if MFA not required, or MFA challenge token if MFA verification needed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Validation failed (invalid email format, blank fields)"
            )
    })
    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @Valid @RequestBody AuthenticationRequestDTO authRequest
    ) {
        log.info("Authentication request received for email: {}", authRequest.email());
        AuthenticationResponseDTO response = authenticationService.authenticate(authRequest);

        if (response.mfaRequired()) {
            log.info("MFA required for email: {}", authRequest.email());
        } else {
            log.info("Authentication successful (no MFA) for email: {}", authRequest.email());
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Step 2: Verify MFA code and complete authentication.
     *
     * This endpoint is called after receiving an MFA challenge token from the authenticate endpoint.
     * It verifies the MFA code and issues the final JWT access token.
     *
     * REQUEST BODY:
     * {
     *   "mfaChallengeToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
     *   "mfaCode": "123456"
     * }
     *
     * RESPONSE (Success):
     * HTTP 200 OK
     * {
     *   "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
     * }
     *
     * ERROR RESPONSES:
     * - 401 Unauthorized: Invalid MFA code or challenge token
     * - 422 Unprocessable Entity: Validation failed (blank fields)
     *
     * SECURITY CONSIDERATIONS:
     * - Challenge token must be valid and non-expired (5 minute window)
     * - MFA code verification should be rate-limited (implement external rate limiting)
     * - Failed attempts should be logged for security monitoring
     *
     * @param mfaRequest MFA challenge token and verification code
     * @return ResponseEntity with MFAVerificationResponseDTO containing JWT token
     */
    @Operation(
            summary = "Verify MFA code and complete authentication",
            description = "Completes the authentication process by verifying MFA code. Returns JWT token on success."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MFA verification successful, JWT token returned",
                    content = @Content(schema = @Schema(implementation = MFAVerificationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid MFA code or challenge token"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Validation failed (blank fields)"
            )
    })
    @PostMapping("verify-mfa")
    public ResponseEntity<MFAVerificationResponseDTO> verifyMfa(
            @Valid @RequestBody MFAVerificationRequestDTO mfaRequest
    ) {
        log.debug("MFA verification request received");
        MFAVerificationResponseDTO response = authenticationService.verifyMfa(mfaRequest);

        log.info("MFA verification successful, JWT token issued");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

