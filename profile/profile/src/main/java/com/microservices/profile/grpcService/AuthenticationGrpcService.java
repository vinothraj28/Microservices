package com.microservices.profile.grpcService;

import com.microservices.profile.dto.auth.AuthenticationRequestDTO;
import com.microservices.profile.dto.auth.AuthenticationResponseDTO;
import com.microservices.profile.dto.auth.MFAVerificationRequestDTO;
import com.microservices.profile.dto.auth.MFAVerificationResponseDTO;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.InvalidMfaChallengeException;
import com.microservices.profile.exceptions.MfaNotConfiguredException;
import com.microservices.profile.grpc.*;
import com.microservices.profile.models.entities.RefreshToken;
import com.microservices.profile.services.AuthenticationService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.tomcat.websocket.AuthenticationException;

/**
 * gRPC Service for Authentication operations.
 *
 * This is the gRPC server endpoint that handles incoming authentication requests from the Gateway.
 * Acts as an adapter between gRPC protocol and the application layer AuthenticationService.
 *
 * Maps between:
 * - gRPC messages (protocol buffers)
 * - Application DTOs (domain layer)
 * - Exceptions to gRPC Status codes
 *
 * @see com.microservices.profile.services.AuthenticationService for business logic
 */
@Slf4j
@GrpcService
public class AuthenticationGrpcService extends AuthenticationServiceGrpc.AuthenticationServiceImplBase {

    private final AuthenticationService authenticationService;

    public AuthenticationGrpcService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Step 1: Handle gRPC Authenticate request.
     *
     * Called by Gateway when user attempts to authenticate with email and password.
     *
     * FLOW:
     * 1. Receive gRPC AuthenticationRequest (email + password)
     * 2. Convert to DTO
     * 3. Call AuthenticationService.authenticate()
     * 4. Convert response DTO to gRPC response
     * 5. Send back via StreamObserver
     *
     * ERROR HANDLING:
     * - InvalidCredentialsException → UNAUTHENTICATED status
     * - All other exceptions → INTERNAL status
     *
     * @param request gRPC AuthenticationRequest (email + password)
     * @param responseObserver StreamObserver to send gRPC response
     */
    @Override
    public void authenticate(
            AuthenticationRequest request,
            StreamObserver<AuthenticationResponse> responseObserver
    ) {
        log.info("gRPC Authenticate request received for email: {}", request.getEmail());

        try {
            // Convert gRPC request to application DTO
            AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(
                    request.getEmail(),
                    request.getPassword()
            );

            // Call application service
            AuthenticationResponseDTO authResponse = authenticationService.authenticate(authRequest);

            log.debug("Authentication processed, building gRPC response");

            // Convert DTO back to gRPC response
            AuthenticationResponse.Builder builder = AuthenticationResponse.newBuilder()
                    .setAccessToken(authResponse.accessToken() != null ? authResponse.accessToken() : "")
                    .setMfaChallengeToken(authResponse.mfaChallengeToken() != null ? authResponse.mfaChallengeToken() : "")
                    .setRefreshToken(authResponse.refreshToken() != null ? authResponse.refreshToken() : "")
                    .setMfaRequired(authResponse.mfaRequired());

            if (authResponse.refreshToken() != null) {
                builder.setRefreshToken(authResponse.refreshToken());
            }

            AuthenticationResponse grpcResponse = builder.build();

            log.info("Sending authentication response to gateway");
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();

        } catch (InvalidCredentialsException ex) {
            log.warn("Authentication failed: {}", ex.getMessage());
            responseObserver.onError(
                    Status.UNAUTHENTICATED
                            .withDescription(ex.getMessage())
                            .asException()
            );
        } catch (Exception ex) {
            log.error("Unexpected error during authentication", ex);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .asException()
            );
        }
    }

    /**
     * Step 2: Handle gRPC VerifyMfa request.
     *
     * Called by Gateway when user attempts to verify MFA code.
     *
     * FLOW:
     * 1. Receive gRPC MfaVerificationRequest (challenge token + code)
     * 2. Convert to DTO
     * 3. Call AuthenticationService.verifyMfa()
     * 4. Convert response DTO to gRPC response
     * 5. Send back via StreamObserver
     *
     * ERROR HANDLING:
     * - InvalidCredentialsException → UNAUTHENTICATED (invalid code)
     * - InvalidMfaChallengeException → UNAUTHENTICATED (expired token)
     * - MfaNotConfiguredException → FAILED_PRECONDITION (state error)
     * - All other exceptions → INTERNAL
     *
     * @param request gRPC MfaVerificationRequest (challenge token + code)
     * @param responseObserver StreamObserver to send gRPC response
     */
    @Override
    public void verifyMfa(
            MfaVerificationRequest request,
            StreamObserver<MfaVerificationResponse> responseObserver
    ) {
        log.debug("gRPC VerifyMfa request received");

        try {
            // Convert gRPC request to application DTO
            MFAVerificationRequestDTO mfaRequest = new MFAVerificationRequestDTO(
                    request.getMfaChallengeToken(),
                    request.getMfaCode()
            );

            // Call application service
            MFAVerificationResponseDTO mfaResponse = authenticationService.verifyMfa(mfaRequest);

            log.debug("MFA verification processed, building gRPC response");

            // Convert DTO back to gRPC response
            MfaVerificationResponse grpcResponse = MfaVerificationResponse.newBuilder()
                    .setAccessToken(mfaResponse.accessToken())
                    .build();

            log.info("Sending MFA verification response to gateway");
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();

        } catch (InvalidCredentialsException ex) {
            log.warn("MFA verification failed: {}", ex.getMessage());
            responseObserver.onError(
                    Status.UNAUTHENTICATED
                            .withDescription(ex.getMessage())
                            .asException()
            );
        } catch (InvalidMfaChallengeException ex) {
            log.warn("MFA challenge verification failed: {}", ex.getMessage());
            responseObserver.onError(
                    Status.UNAUTHENTICATED
                            .withDescription(ex.getMessage())
                            .asException()
            );
        } catch (MfaNotConfiguredException ex) {
            log.error("MFA not configured for user: {}", ex.getMessage());
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription(ex.getMessage())
                            .asException()
            );
        } catch (Exception ex) {
            log.error("Unexpected error during MFA verification", ex);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .asException()
            );
        }
    }

    @Override
    public void refreshToken(RefreshTokenRequest request,
                             StreamObserver<RefreshTokenResponse> responseObserver){

        try{
            AuthenticationResponseDTO authenticationResponseDTO =
                    authenticationService.refreshAccessToken(request.getRefreshToken());

            RefreshTokenResponse grpcResponse = RefreshTokenResponse.newBuilder()
                    .setAccessToken(authenticationResponseDTO.accessToken())
                    .setRefreshToken(authenticationResponseDTO.refreshToken())
                    .build();
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();

        }catch (Exception ex){
            log.warn("Failed to refresh token", ex);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Failed to refresh token")
                            .asException()
            );
        }

    }

    @Override
    public void logout(LogoutRequest request,
                       StreamObserver<LogoutResponse> responseObserver){
        try{
            authenticationService.logout(request.getRefreshToken());
            LogoutResponse response = LogoutResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Logout successful")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (Exception ex){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Logout failed")
                            .asException()
            );
        }
    }
}

