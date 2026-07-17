package com.microservices.gateway.services.gRPCServices;

import com.microservices.gateway.DTOS.auth.AuthenticationRequestDTO;
import com.microservices.gateway.DTOS.auth.AuthenticationResponseDTO;
import com.microservices.gateway.DTOS.auth.RefreshTokenResponseDTO;
import com.microservices.gateway.DTOS.mfa.MfaVerificationRequestDTO;
import com.microservices.gateway.DTOS.mfa.MfaVerificationResponseDTO;
import com.microservices.gateway.excpetions.AuthenticationException;
import com.microservices.profile.grpc.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * gRPC client service for Authentication operations.
 *
 * Communicates with Profile Service's AuthenticationService via gRPC.
 * Translates between gateway DTOs and gRPC messages.
 * Handles error mapping for gRPC status codes.
 */
@Slf4j
@Service
public class AuthenticationGRPCService {

    @GrpcClient("profile-service")
    private AuthenticationServiceGrpc.AuthenticationServiceBlockingStub authenticationServiceBlockingStub;

    /**
     * Step 1: Authenticate user with email and password.
     *
     * Calls the Profile Service's Authenticate RPC method.
     * Returns either:
     * - Access token (if MFA not required)
     * - MFA challenge token (if MFA required)
     *
     * @param request Authentication request (email + password)
     * @return Authentication response (token or MFA challenge)
     * @throws AuthenticationException if credentials are invalid
     */
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        log.info("Authenticating user via gRPC: {}", request.email());

        try {
            AuthenticationRequest grpcRequest =
                    AuthenticationRequest.newBuilder()
                            .setEmail(request.email())
                            .setPassword(request.password())
                            .build();

            log.debug("Sending authentication request to Profile Service");
            AuthenticationResponse grpcResponse =
                    authenticationServiceBlockingStub.authenticate(grpcRequest);

            log.info("Authentication response received for user: {}", request.email());

            // Map gRPC response to DTO (including optional refresh token)
            String access = grpcResponse.getAccessToken().isEmpty() ? null : grpcResponse.getAccessToken();
            String mfa = grpcResponse.getMfaChallengeToken().isEmpty() ? null : grpcResponse.getMfaChallengeToken();
            String refresh = grpcResponse.getRefreshToken().isEmpty() ? null : grpcResponse.getRefreshToken();

            return new AuthenticationResponseDTO(access, mfa, refresh, grpcResponse.getMfaRequired());

        } catch (StatusRuntimeException ex) {
            log.warn("Authentication failed: {}", ex.getStatus().getDescription());
            handleGrpcException(ex);
            throw new AuthenticationException(ex.getStatus().getDescription());
        }
    }

    /**
     * Step 2: Verify MFA code and complete authentication.
     *
     * Calls the Profile Service's VerifyMfa RPC method.
     * Returns the final JWT access token on success.
     *
     * @param request MFA verification request (challenge token + code)
     * @return MFA verification response (access token)
     * @throws AuthenticationException if MFA code is invalid or challenge expired
     */
    public MfaVerificationResponseDTO verifyMfa(MfaVerificationRequestDTO request) {
        log.debug("Verifying MFA code via gRPC");

        try {
            MfaVerificationRequest grpcRequest =
                    MfaVerificationRequest.newBuilder()
                            .setMfaChallengeToken(request.mfaChallengeToken())
                            .setMfaCode(request.mfaCode())
                            .build();

            log.debug("Sending MFA verification request to Profile Service");
            MfaVerificationResponse grpcResponse =
                    authenticationServiceBlockingStub.verifyMfa(grpcRequest);

            log.info("MFA verification successful, token issued");

            return new MfaVerificationResponseDTO(grpcResponse.getAccessToken());
        } catch (StatusRuntimeException ex) {
            log.warn("MFA verification failed: {}", ex.getStatus().getDescription());
            handleGrpcException(ex);
            throw new AuthenticationException(ex.getStatus().getDescription());
        }
    }

    public RefreshTokenResponseDTO refreshToken(String refreshToken){
        log.debug("Refreshing token via gRPC");

        try{
            RefreshTokenRequest request = RefreshTokenRequest.newBuilder()
                    .setRefreshToken(refreshToken)
                    .build();

            RefreshTokenResponse refreshTokenResponse = authenticationServiceBlockingStub.refreshToken(request);

            return new RefreshTokenResponseDTO(refreshTokenResponse.getAccessToken(), refreshTokenResponse.getRefreshToken());
        }catch (StatusRuntimeException e){
            log.warn("Failed to refresh token: {}", e.getMessage());
            handleGrpcException(e);
            throw new AuthenticationException("Failed to refresh token");
        }

    }

    public LogoutResponse logout(String refreshToken) {
        try{
            LogoutRequest loginRequest = LogoutRequest.newBuilder()
                    .setRefreshToken(refreshToken)
                    .build();

            LogoutResponse response = authenticationServiceBlockingStub.logout(loginRequest);
            log.info("Logout response received: {}", response.getMessage());
            return response;
        }catch (StatusRuntimeException ex){
            log.warn("Logout failed: {}", ex.getStatus().getDescription());
            throw new AuthenticationException("Logout failed");
        }
    }

    /**
     * Map gRPC exceptions to domain exceptions.
     *
     * Provides detailed logging for debugging.
     * Different gRPC status codes map to different error scenarios.
     *
     * @param ex The gRPC StatusRuntimeException
     */
    private void handleGrpcException(StatusRuntimeException ex) {
        Status.Code code = ex.getStatus().getCode();
        String description = ex.getStatus().getDescription();

        switch (code) {
            case INVALID_ARGUMENT -> log.warn("Invalid authentication argument: {}", description);
            case NOT_FOUND -> log.warn("User not found: {}", description);
            case UNAUTHENTICATED -> log.warn("Unauthenticated: {}", description);
            case PERMISSION_DENIED -> log.warn("Permission denied: {}", description);
            default -> log.error("Unexpected gRPC error code {}: {}", code, description);
        }
    }


}

