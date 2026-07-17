package com.microservices.profile.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: UserProfile.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthenticationServiceGrpc {

  private AuthenticationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "user.AuthenticationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.profile.grpc.AuthenticationRequest,
      com.microservices.profile.grpc.AuthenticationResponse> getAuthenticateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authenticate",
      requestType = com.microservices.profile.grpc.AuthenticationRequest.class,
      responseType = com.microservices.profile.grpc.AuthenticationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.profile.grpc.AuthenticationRequest,
      com.microservices.profile.grpc.AuthenticationResponse> getAuthenticateMethod() {
    io.grpc.MethodDescriptor<com.microservices.profile.grpc.AuthenticationRequest, com.microservices.profile.grpc.AuthenticationResponse> getAuthenticateMethod;
    if ((getAuthenticateMethod = AuthenticationServiceGrpc.getAuthenticateMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getAuthenticateMethod = AuthenticationServiceGrpc.getAuthenticateMethod) == null) {
          AuthenticationServiceGrpc.getAuthenticateMethod = getAuthenticateMethod =
              io.grpc.MethodDescriptor.<com.microservices.profile.grpc.AuthenticationRequest, com.microservices.profile.grpc.AuthenticationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Authenticate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.AuthenticationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.AuthenticationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("Authenticate"))
              .build();
        }
      }
    }
    return getAuthenticateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaVerificationRequest,
      com.microservices.profile.grpc.MfaVerificationResponse> getVerifyMfaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyMfa",
      requestType = com.microservices.profile.grpc.MfaVerificationRequest.class,
      responseType = com.microservices.profile.grpc.MfaVerificationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaVerificationRequest,
      com.microservices.profile.grpc.MfaVerificationResponse> getVerifyMfaMethod() {
    io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaVerificationRequest, com.microservices.profile.grpc.MfaVerificationResponse> getVerifyMfaMethod;
    if ((getVerifyMfaMethod = AuthenticationServiceGrpc.getVerifyMfaMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getVerifyMfaMethod = AuthenticationServiceGrpc.getVerifyMfaMethod) == null) {
          AuthenticationServiceGrpc.getVerifyMfaMethod = getVerifyMfaMethod =
              io.grpc.MethodDescriptor.<com.microservices.profile.grpc.MfaVerificationRequest, com.microservices.profile.grpc.MfaVerificationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyMfa"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.MfaVerificationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.MfaVerificationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("VerifyMfa"))
              .build();
        }
      }
    }
    return getVerifyMfaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.profile.grpc.RefreshTokenRequest,
      com.microservices.profile.grpc.RefreshTokenResponse> getRefreshTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RefreshToken",
      requestType = com.microservices.profile.grpc.RefreshTokenRequest.class,
      responseType = com.microservices.profile.grpc.RefreshTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.profile.grpc.RefreshTokenRequest,
      com.microservices.profile.grpc.RefreshTokenResponse> getRefreshTokenMethod() {
    io.grpc.MethodDescriptor<com.microservices.profile.grpc.RefreshTokenRequest, com.microservices.profile.grpc.RefreshTokenResponse> getRefreshTokenMethod;
    if ((getRefreshTokenMethod = AuthenticationServiceGrpc.getRefreshTokenMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getRefreshTokenMethod = AuthenticationServiceGrpc.getRefreshTokenMethod) == null) {
          AuthenticationServiceGrpc.getRefreshTokenMethod = getRefreshTokenMethod =
              io.grpc.MethodDescriptor.<com.microservices.profile.grpc.RefreshTokenRequest, com.microservices.profile.grpc.RefreshTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RefreshToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.RefreshTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.RefreshTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("RefreshToken"))
              .build();
        }
      }
    }
    return getRefreshTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.profile.grpc.LogoutRequest,
      com.microservices.profile.grpc.LogoutResponse> getLogoutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Logout",
      requestType = com.microservices.profile.grpc.LogoutRequest.class,
      responseType = com.microservices.profile.grpc.LogoutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.profile.grpc.LogoutRequest,
      com.microservices.profile.grpc.LogoutResponse> getLogoutMethod() {
    io.grpc.MethodDescriptor<com.microservices.profile.grpc.LogoutRequest, com.microservices.profile.grpc.LogoutResponse> getLogoutMethod;
    if ((getLogoutMethod = AuthenticationServiceGrpc.getLogoutMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getLogoutMethod = AuthenticationServiceGrpc.getLogoutMethod) == null) {
          AuthenticationServiceGrpc.getLogoutMethod = getLogoutMethod =
              io.grpc.MethodDescriptor.<com.microservices.profile.grpc.LogoutRequest, com.microservices.profile.grpc.LogoutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Logout"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.LogoutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.LogoutResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("Logout"))
              .build();
        }
      }
    }
    return getLogoutMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthenticationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceStub>() {
        @java.lang.Override
        public AuthenticationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthenticationServiceStub(channel, callOptions);
        }
      };
    return AuthenticationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthenticationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceBlockingStub>() {
        @java.lang.Override
        public AuthenticationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthenticationServiceBlockingStub(channel, callOptions);
        }
      };
    return AuthenticationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthenticationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceFutureStub>() {
        @java.lang.Override
        public AuthenticationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthenticationServiceFutureStub(channel, callOptions);
        }
      };
    return AuthenticationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void authenticate(com.microservices.profile.grpc.AuthenticationRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.AuthenticationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAuthenticateMethod(), responseObserver);
    }

    /**
     */
    default void verifyMfa(com.microservices.profile.grpc.MfaVerificationRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaVerificationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyMfaMethod(), responseObserver);
    }

    /**
     */
    default void refreshToken(com.microservices.profile.grpc.RefreshTokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.RefreshTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRefreshTokenMethod(), responseObserver);
    }

    /**
     */
    default void logout(com.microservices.profile.grpc.LogoutRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.LogoutResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLogoutMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthenticationService.
   */
  public static abstract class AuthenticationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthenticationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthenticationService.
   */
  public static final class AuthenticationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AuthenticationServiceStub> {
    private AuthenticationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthenticationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthenticationServiceStub(channel, callOptions);
    }

    /**
     */
    public void authenticate(com.microservices.profile.grpc.AuthenticationRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.AuthenticationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAuthenticateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void verifyMfa(com.microservices.profile.grpc.MfaVerificationRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaVerificationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyMfaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void refreshToken(com.microservices.profile.grpc.RefreshTokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.RefreshTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRefreshTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void logout(com.microservices.profile.grpc.LogoutRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.LogoutResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthenticationService.
   */
  public static final class AuthenticationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthenticationServiceBlockingStub> {
    private AuthenticationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthenticationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthenticationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.profile.grpc.AuthenticationResponse authenticate(com.microservices.profile.grpc.AuthenticationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAuthenticateMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.profile.grpc.MfaVerificationResponse verifyMfa(com.microservices.profile.grpc.MfaVerificationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyMfaMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.profile.grpc.RefreshTokenResponse refreshToken(com.microservices.profile.grpc.RefreshTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRefreshTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.profile.grpc.LogoutResponse logout(com.microservices.profile.grpc.LogoutRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLogoutMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthenticationService.
   */
  public static final class AuthenticationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthenticationServiceFutureStub> {
    private AuthenticationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthenticationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthenticationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.profile.grpc.AuthenticationResponse> authenticate(
        com.microservices.profile.grpc.AuthenticationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAuthenticateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.profile.grpc.MfaVerificationResponse> verifyMfa(
        com.microservices.profile.grpc.MfaVerificationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyMfaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.profile.grpc.RefreshTokenResponse> refreshToken(
        com.microservices.profile.grpc.RefreshTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRefreshTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.profile.grpc.LogoutResponse> logout(
        com.microservices.profile.grpc.LogoutRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AUTHENTICATE = 0;
  private static final int METHODID_VERIFY_MFA = 1;
  private static final int METHODID_REFRESH_TOKEN = 2;
  private static final int METHODID_LOGOUT = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AUTHENTICATE:
          serviceImpl.authenticate((com.microservices.profile.grpc.AuthenticationRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.profile.grpc.AuthenticationResponse>) responseObserver);
          break;
        case METHODID_VERIFY_MFA:
          serviceImpl.verifyMfa((com.microservices.profile.grpc.MfaVerificationRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaVerificationResponse>) responseObserver);
          break;
        case METHODID_REFRESH_TOKEN:
          serviceImpl.refreshToken((com.microservices.profile.grpc.RefreshTokenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.profile.grpc.RefreshTokenResponse>) responseObserver);
          break;
        case METHODID_LOGOUT:
          serviceImpl.logout((com.microservices.profile.grpc.LogoutRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.profile.grpc.LogoutResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAuthenticateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.profile.grpc.AuthenticationRequest,
              com.microservices.profile.grpc.AuthenticationResponse>(
                service, METHODID_AUTHENTICATE)))
        .addMethod(
          getVerifyMfaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.profile.grpc.MfaVerificationRequest,
              com.microservices.profile.grpc.MfaVerificationResponse>(
                service, METHODID_VERIFY_MFA)))
        .addMethod(
          getRefreshTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.profile.grpc.RefreshTokenRequest,
              com.microservices.profile.grpc.RefreshTokenResponse>(
                service, METHODID_REFRESH_TOKEN)))
        .addMethod(
          getLogoutMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.profile.grpc.LogoutRequest,
              com.microservices.profile.grpc.LogoutResponse>(
                service, METHODID_LOGOUT)))
        .build();
  }

  private static abstract class AuthenticationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthenticationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.profile.grpc.UserProfile.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthenticationService");
    }
  }

  private static final class AuthenticationServiceFileDescriptorSupplier
      extends AuthenticationServiceBaseDescriptorSupplier {
    AuthenticationServiceFileDescriptorSupplier() {}
  }

  private static final class AuthenticationServiceMethodDescriptorSupplier
      extends AuthenticationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AuthenticationServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthenticationServiceFileDescriptorSupplier())
              .addMethod(getAuthenticateMethod())
              .addMethod(getVerifyMfaMethod())
              .addMethod(getRefreshTokenMethod())
              .addMethod(getLogoutMethod())
              .build();
        }
      }
    }
    return result;
  }
}
