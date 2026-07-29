package com.microservices.profile.grpcService;

import com.microservices.oauth2.grpc.AuthorizeRequest;
import com.microservices.oauth2.grpc.AuthorizeResponse;
import com.microservices.oauth2.grpc.ClientType;
import com.microservices.oauth2.grpc.GrantType;
import com.microservices.oauth2.grpc.IntrospectTokenRequest;
import com.microservices.oauth2.grpc.IntrospectTokenResponse;
import com.microservices.oauth2.grpc.OAuth2ServiceGrpc;
import com.microservices.oauth2.grpc.RegisterClientRequest;
import com.microservices.oauth2.grpc.RegisterClientResponse;
import com.microservices.oauth2.grpc.RevokeTokenRequest;
import com.microservices.oauth2.grpc.RevokeTokenResponse;
import com.microservices.oauth2.grpc.TokenRequest;
import com.microservices.oauth2.grpc.TokenResponse;
import com.microservices.profile.exceptions.OAuth2ValidationException;
import com.microservices.profile.models.entities.OAuth2Client;
import com.microservices.profile.services.OAuth2ClientService;
import com.microservices.profile.services.OAuth2TokenService;
import com.microservices.profile.services.TokenService;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@GrpcService
public class OAuth2GrpcService extends OAuth2ServiceGrpc.OAuth2ServiceImplBase {

    private static final String REFRESH_TOKEN = "refresh_token";

    private final OAuth2ClientService oAuth2ClientService;
    private final com.microservices.profile.services.OAuth2AuthorizationCodeService oAuth2AuthorizationCodeService;
    private final OAuth2TokenService oAuth2TokenService;
    private final TokenService tokenService;

    public OAuth2GrpcService(
            OAuth2ClientService oAuth2ClientService,
            com.microservices.profile.services.OAuth2AuthorizationCodeService oAuth2AuthorizationCodeService,
            OAuth2TokenService oAuth2TokenService,
            TokenService tokenService
    ) {
        this.oAuth2ClientService = oAuth2ClientService;
        this.oAuth2AuthorizationCodeService = oAuth2AuthorizationCodeService;
        this.oAuth2TokenService = oAuth2TokenService;
        this.tokenService = tokenService;
    }

    @Override
    public void registerClient(RegisterClientRequest request, StreamObserver<RegisterClientResponse> responseObserver) {
        try {
            List<com.microservices.profile.models.enums.GrantType> grantTypes = request.getGrantTypesList().stream()
                    .filter(gt -> gt != GrantType.GRANT_TYPE_UNSPECIFIED)
                    .map(gt -> com.microservices.profile.models.enums.GrantType.valueOf(gt.name()))
                    .toList();

            OAuth2ClientService.RegisteredClient registeredClient = oAuth2ClientService.registerClient(
                    request.getClientName(),
                    request.getRedirectUrisList(),
                    request.getScopesList(),
                    grantTypes,
                    mapClientType(request.getClientType()),
                    request.getApplicationType()
            );

            RegisterClientResponse response = RegisterClientResponse.newBuilder()
                    .setClientId(registeredClient.clientId())
                    .setClientSecret(registeredClient.clientSecret() == null ? "" : registeredClient.clientSecret())
                    .setClientName(registeredClient.client().getClientName())
                    .addAllRedirectUris(registeredClient.client().getRedirectUris())
                    .addAllScopes(registeredClient.client().getAllowedScopes())
                    .setClientType(request.getClientType())
                    .addAllGrantTypes(request.getGrantTypesList())
                    .setCreatedAt(nowTimestamp())
                    .setClientSecretExpiresAt(0L)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (OAuth2ValidationException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asException());
        } catch (Exception ex) {
            log.error("registerClient failed", ex);
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to register OAuth2 client").asException());
        }
    }

    @Override
    public void authorize(AuthorizeRequest request, StreamObserver<AuthorizeResponse> responseObserver) {
        try {
            if (!"code".equalsIgnoreCase(request.getResponseType())) {
                throw new OAuth2ValidationException("Only response_type=code is supported");
            }

            OAuth2Client client = oAuth2ClientService.getActiveClient(request.getClientId());
            oAuth2ClientService.validateRedirectUri(client, request.getRedirectUri());
            Set<String> scopes = oAuth2ClientService.validateAndNormalizeScopes(client, request.getScope());

            String authorizationCode = oAuth2AuthorizationCodeService.createAuthorizationCode(
                    request.getUserId(),
                    client.getClientId(),
                    request.getRedirectUri(),
                    String.join(" ", scopes),
                    request.getCodeChallenge(),
                    request.getCodeChallengeMethod(),
                    request.getNonce()
            );

            String redirectUri = request.getRedirectUri() + "?code=" + authorizationCode + "&state=" + request.getState();

            AuthorizeResponse response = AuthorizeResponse.newBuilder()
                    .setAuthorizationCode(authorizationCode)
                    .setState(request.getState())
                    .setRedirectUri(redirectUri)
                    .setExpiresIn(600)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid user_id").asException());
        } catch (OAuth2ValidationException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asException());
        } catch (Exception ex) {
            log.error("authorize failed", ex);
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to authorize client").asException());
        }
    }

    @Override
    public void token(TokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        try {
            OAuth2Client client = oAuth2ClientService.validateClientCredentials(
                    request.getClientId(),
                    request.hasClientSecret() ? request.getClientSecret() : null
            );

            OAuth2TokenService.TokenResult tokenResult;
            if (request.getGrantType() == GrantType.AUTHORIZATION_CODE) {
                tokenResult = oAuth2TokenService.exchangeAuthorizationCode(
                        request.getCode(),
                        client.getClientId(),
                        request.getRedirectUri(),
                        request.getCodeVerifier()
                );
            } else if (request.getGrantType() == GrantType.REFRESH_TOKEN) {
                tokenResult = oAuth2TokenService.refreshTokens(
                        request.getRefreshToken(),
                        client.getClientId(),
                        request.hasScope() ? request.getScope() : null
                );
            } else {
                throw new OAuth2ValidationException("Unsupported grant_type");
            }

            TokenResponse response = TokenResponse.newBuilder()
                    .setAccessToken(tokenResult.accessToken())
                    .setTokenType(tokenResult.tokenType())
                    .setExpiresIn((int) tokenResult.expiresIn())
                    .setRefreshToken(tokenResult.refreshToken())
                    .setScope(tokenResult.scope())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (OAuth2ValidationException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asException());
        } catch (Exception ex) {
            log.error("token failed", ex);
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to issue token").asException());
        }
    }

    @Override
    public void introspectToken(IntrospectTokenRequest request, StreamObserver<IntrospectTokenResponse> responseObserver) {
        try {
            oAuth2ClientService.validateClientCredentials(
                    request.getClientId(),
                    request.hasClientSecret() ? request.getClientSecret() : null
            );

            boolean active = tokenService.isValid(request.getToken()) && !oAuth2TokenService.isAccessTokenRevoked(request.getToken());
            IntrospectTokenResponse.Builder builder = IntrospectTokenResponse.newBuilder().setActive(active);

            if (active) {
                var claims = tokenService.extractClaims(request.getToken());
                builder.setSub(claims.getSubject() == null ? "" : claims.getSubject());
                builder.setClientId(request.getClientId());
                builder.setTokenType("Bearer");
                builder.setExp(claims.getExpiration() == null ? 0L : claims.getExpiration().toInstant().getEpochSecond());
                builder.setIat(claims.getIssuedAt() == null ? 0L : claims.getIssuedAt().toInstant().getEpochSecond());
                builder.setJti(claims.getId() == null ? "" : claims.getId());
                Object scope = claims.get("scope");
                if (scope instanceof String scopeString) {
                    builder.setScope(scopeString);
                }
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (OAuth2ValidationException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asException());
        } catch (Exception ex) {
            log.error("introspectToken failed", ex);
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to introspect token").asException());
        }
    }

    @Override
    public void revokeToken(RevokeTokenRequest request, StreamObserver<RevokeTokenResponse> responseObserver) {
        try {
            oAuth2ClientService.validateClientCredentials(
                    request.getClientId(),
                    request.hasClientSecret() ? request.getClientSecret() : null
            );

            String hint = request.hasTokenTypeHint() ? request.getTokenTypeHint() : "";
            if (REFRESH_TOKEN.equalsIgnoreCase(hint)) {
                oAuth2TokenService.revokeRefreshToken(request.getToken(), "oauth2_revoke");
            } else {
                oAuth2TokenService.revokeAccessToken(request.getToken(), request.getClientId(), "oauth2_revoke");
            }

            responseObserver.onNext(RevokeTokenResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Token revoked")
                    .build());
            responseObserver.onCompleted();
        } catch (OAuth2ValidationException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asException());
        } catch (Exception ex) {
            log.error("revokeToken failed", ex);
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to revoke token").asException());
        }
    }

    private com.microservices.profile.models.enums.ClientType mapClientType(ClientType clientType) {
        if (clientType == null || clientType == ClientType.CLIENT_TYPE_UNSPECIFIED) {
            throw new OAuth2ValidationException("client_type is required");
        }
        return com.microservices.profile.models.enums.ClientType.valueOf(clientType.name());
    }

    private Timestamp nowTimestamp() {
        Instant now = Instant.now();
        return Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();
    }
}
