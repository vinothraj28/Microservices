package com.microservices.gateway.services.gRPCServices;

import com.microservices.gateway.DTOS.oauth2.OAuth2AuthorizeResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2ClientRegistrationRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2ClientRegistrationResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2IntrospectRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2IntrospectResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2RevokeRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2RevokeResponseDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2TokenRequestDTO;
import com.microservices.gateway.DTOS.oauth2.OAuth2TokenResponseDTO;
import com.microservices.gateway.excpetions.AuthenticationException;
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
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class OAuth2GRPCService {

    @GrpcClient("profile-service")
    private OAuth2ServiceGrpc.OAuth2ServiceBlockingStub oAuth2ServiceBlockingStub;

    public OAuth2ClientRegistrationResponseDTO registerClient(OAuth2ClientRegistrationRequestDTO request) {
        try {
            RegisterClientRequest grpcRequest = RegisterClientRequest.newBuilder()
                    .setClientName(request.clientName())
                    .addAllRedirectUris(request.redirectUris())
                    .addAllScopes(request.scopes())
                    .setClientType(ClientType.valueOf(request.clientType().trim().toUpperCase(Locale.ROOT)))
                    .addAllGrantTypes(request.grantTypes().stream()
                            .map(g -> GrantType.valueOf(g.trim().toUpperCase(Locale.ROOT)))
                            .toList())
                    .setApplicationType(request.applicationType() == null ? "" : request.applicationType())
                    .build();

            RegisterClientResponse response = oAuth2ServiceBlockingStub.registerClient(grpcRequest);
            return new OAuth2ClientRegistrationResponseDTO(
                    response.getClientId(),
                    response.getClientSecret().isBlank() ? null : response.getClientSecret(),
                    response.getClientName(),
                    response.getRedirectUrisList(),
                    response.getScopesList(),
                    response.getClientType().name(),
                    response.getGrantTypesList().stream().map(Enum::name).toList(),
                    response.getCreatedAt().getSeconds()
            );
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationException("Invalid OAuth2 client_type or grant_types", ex);
        } catch (StatusRuntimeException ex) {
            throw mapGrpcError(ex);
        }
    }

    public OAuth2AuthorizeResponseDTO authorize(
            String responseType,
            String clientId,
            String redirectUri,
            String scope,
            String state,
            String codeChallenge,
            String codeChallengeMethod,
            String userId,
            String nonce
    ) {
        try {
            AuthorizeRequest grpcRequest = AuthorizeRequest.newBuilder()
                    .setResponseType(responseType)
                    .setClientId(clientId)
                    .setRedirectUri(redirectUri)
                    .setScope(scope == null ? "" : scope)
                    .setState(state == null ? "" : state)
                    .setCodeChallenge(codeChallenge == null ? "" : codeChallenge)
                    .setCodeChallengeMethod(codeChallengeMethod == null ? "" : codeChallengeMethod)
                    .setUserId(userId)
                    .setNonce(nonce == null ? "" : nonce)
                    .build();

            AuthorizeResponse response = oAuth2ServiceBlockingStub.authorize(grpcRequest);
            return new OAuth2AuthorizeResponseDTO(
                    response.getAuthorizationCode(),
                    response.getState(),
                    response.getRedirectUri(),
                    response.getExpiresIn()
            );
        } catch (StatusRuntimeException ex) {
            throw mapGrpcError(ex);
        }
    }

    public OAuth2TokenResponseDTO token(OAuth2TokenRequestDTO request) {
        try {
            TokenRequest.Builder builder = TokenRequest.newBuilder()
                    .setClientId(request.clientId())
                    .setGrantType(mapGrantType(request.grantType()));

            if (request.clientSecret() != null && !request.clientSecret().isBlank()) {
                builder.setClientSecret(request.clientSecret());
            }
            if (request.code() != null && !request.code().isBlank()) {
                builder.setCode(request.code());
            }
            if (request.redirectUri() != null && !request.redirectUri().isBlank()) {
                builder.setRedirectUri(request.redirectUri());
            }
            if (request.codeVerifier() != null && !request.codeVerifier().isBlank()) {
                builder.setCodeVerifier(request.codeVerifier());
            }
            if (request.refreshToken() != null && !request.refreshToken().isBlank()) {
                builder.setRefreshToken(request.refreshToken());
            }
            if (request.scope() != null && !request.scope().isBlank()) {
                builder.setScope(request.scope());
            }

            TokenResponse response = oAuth2ServiceBlockingStub.token(builder.build());
            return new OAuth2TokenResponseDTO(
                    response.getAccessToken(),
                    response.getTokenType(),
                    response.getExpiresIn(),
                    response.getRefreshToken().isBlank() ? null : response.getRefreshToken(),
                    response.getScope().isBlank() ? null : response.getScope()
            );
        } catch (StatusRuntimeException ex) {
            throw mapGrpcError(ex);
        }
    }

    public OAuth2IntrospectResponseDTO introspect(OAuth2IntrospectRequestDTO request) {
        try {
            IntrospectTokenRequest.Builder builder = IntrospectTokenRequest.newBuilder()
                    .setToken(request.token())
                    .setClientId(request.clientId());

            if (request.clientSecret() != null && !request.clientSecret().isBlank()) {
                builder.setClientSecret(request.clientSecret());
            }
            if (request.tokenTypeHint() != null && !request.tokenTypeHint().isBlank()) {
                builder.setTokenTypeHint(request.tokenTypeHint());
            }

            IntrospectTokenResponse response = oAuth2ServiceBlockingStub.introspectToken(builder.build());
            return new OAuth2IntrospectResponseDTO(
                    response.getActive(),
                    response.hasScope() ? response.getScope() : null,
                    response.hasClientId() ? response.getClientId() : null,
                    response.hasTokenType() ? response.getTokenType() : null,
                    response.hasExp() ? response.getExp() : 0L,
                    response.hasIat() ? response.getIat() : 0L,
                    response.hasSub() ? response.getSub() : null,
                    response.hasJti() ? response.getJti() : null
            );
        } catch (StatusRuntimeException ex) {
            throw mapGrpcError(ex);
        }
    }

    public OAuth2RevokeResponseDTO revoke(OAuth2RevokeRequestDTO request) {
        try {
            RevokeTokenRequest.Builder builder = RevokeTokenRequest.newBuilder()
                    .setToken(request.token())
                    .setClientId(request.clientId());

            if (request.clientSecret() != null && !request.clientSecret().isBlank()) {
                builder.setClientSecret(request.clientSecret());
            }
            if (request.tokenTypeHint() != null && !request.tokenTypeHint().isBlank()) {
                builder.setTokenTypeHint(request.tokenTypeHint());
            }

            RevokeTokenResponse response = oAuth2ServiceBlockingStub.revokeToken(builder.build());
            return new OAuth2RevokeResponseDTO(response.getSuccess(), response.getMessage());
        } catch (StatusRuntimeException ex) {
            throw mapGrpcError(ex);
        }
    }

    private GrantType mapGrantType(String grantType) {
        if (grantType == null) {
            throw new AuthenticationException("grantType is required");
        }
        return switch (grantType.trim().toLowerCase(Locale.ROOT)) {
            case "authorization_code" -> GrantType.AUTHORIZATION_CODE;
            case "refresh_token" -> GrantType.REFRESH_TOKEN;
            case "client_credentials" -> GrantType.CLIENT_CREDENTIALS;
            case "password" -> GrantType.PASSWORD;
            case "implicit" -> GrantType.IMPLICIT;
            default -> throw new AuthenticationException("Unsupported grantType: " + grantType);
        };
    }

    private AuthenticationException mapGrpcError(StatusRuntimeException ex) {
        Status.Code code = ex.getStatus().getCode();
        String description = ex.getStatus().getDescription() == null ? "OAuth2 operation failed" : ex.getStatus().getDescription();
        if (code == Status.Code.INVALID_ARGUMENT || code == Status.Code.UNAUTHENTICATED || code == Status.Code.PERMISSION_DENIED) {
            return new AuthenticationException(description, ex);
        }
        if (code == Status.Code.NOT_FOUND) {
            return new AuthenticationException(description, ex);
        }
        log.error("Unexpected OAuth2 gRPC error: {}", ex.getStatus(), ex);
        return new AuthenticationException("OAuth2 service failure", ex);
    }
}
