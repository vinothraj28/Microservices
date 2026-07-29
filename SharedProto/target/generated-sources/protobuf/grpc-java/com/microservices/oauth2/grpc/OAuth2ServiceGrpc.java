package com.microservices.oauth2.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * OAuth2 Service - Authorization Server operations
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: OAuth2.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class OAuth2ServiceGrpc {

  private OAuth2ServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "oauth2.OAuth2Service";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.RegisterClientRequest,
      com.microservices.oauth2.grpc.RegisterClientResponse> getRegisterClientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterClient",
      requestType = com.microservices.oauth2.grpc.RegisterClientRequest.class,
      responseType = com.microservices.oauth2.grpc.RegisterClientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.RegisterClientRequest,
      com.microservices.oauth2.grpc.RegisterClientResponse> getRegisterClientMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.RegisterClientRequest, com.microservices.oauth2.grpc.RegisterClientResponse> getRegisterClientMethod;
    if ((getRegisterClientMethod = OAuth2ServiceGrpc.getRegisterClientMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getRegisterClientMethod = OAuth2ServiceGrpc.getRegisterClientMethod) == null) {
          OAuth2ServiceGrpc.getRegisterClientMethod = getRegisterClientMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.RegisterClientRequest, com.microservices.oauth2.grpc.RegisterClientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterClient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.RegisterClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.RegisterClientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("RegisterClient"))
              .build();
        }
      }
    }
    return getRegisterClientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.GetClientRequest,
      com.microservices.oauth2.grpc.GetClientResponse> getGetClientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetClient",
      requestType = com.microservices.oauth2.grpc.GetClientRequest.class,
      responseType = com.microservices.oauth2.grpc.GetClientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.GetClientRequest,
      com.microservices.oauth2.grpc.GetClientResponse> getGetClientMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.GetClientRequest, com.microservices.oauth2.grpc.GetClientResponse> getGetClientMethod;
    if ((getGetClientMethod = OAuth2ServiceGrpc.getGetClientMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getGetClientMethod = OAuth2ServiceGrpc.getGetClientMethod) == null) {
          OAuth2ServiceGrpc.getGetClientMethod = getGetClientMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.GetClientRequest, com.microservices.oauth2.grpc.GetClientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetClient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.GetClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.GetClientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("GetClient"))
              .build();
        }
      }
    }
    return getGetClientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.UpdateClientRequest,
      com.microservices.oauth2.grpc.UpdateClientResponse> getUpdateClientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateClient",
      requestType = com.microservices.oauth2.grpc.UpdateClientRequest.class,
      responseType = com.microservices.oauth2.grpc.UpdateClientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.UpdateClientRequest,
      com.microservices.oauth2.grpc.UpdateClientResponse> getUpdateClientMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.UpdateClientRequest, com.microservices.oauth2.grpc.UpdateClientResponse> getUpdateClientMethod;
    if ((getUpdateClientMethod = OAuth2ServiceGrpc.getUpdateClientMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getUpdateClientMethod = OAuth2ServiceGrpc.getUpdateClientMethod) == null) {
          OAuth2ServiceGrpc.getUpdateClientMethod = getUpdateClientMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.UpdateClientRequest, com.microservices.oauth2.grpc.UpdateClientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateClient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.UpdateClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.UpdateClientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("UpdateClient"))
              .build();
        }
      }
    }
    return getUpdateClientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.DeleteClientRequest,
      com.microservices.oauth2.grpc.DeleteClientResponse> getDeleteClientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteClient",
      requestType = com.microservices.oauth2.grpc.DeleteClientRequest.class,
      responseType = com.microservices.oauth2.grpc.DeleteClientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.DeleteClientRequest,
      com.microservices.oauth2.grpc.DeleteClientResponse> getDeleteClientMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.DeleteClientRequest, com.microservices.oauth2.grpc.DeleteClientResponse> getDeleteClientMethod;
    if ((getDeleteClientMethod = OAuth2ServiceGrpc.getDeleteClientMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getDeleteClientMethod = OAuth2ServiceGrpc.getDeleteClientMethod) == null) {
          OAuth2ServiceGrpc.getDeleteClientMethod = getDeleteClientMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.DeleteClientRequest, com.microservices.oauth2.grpc.DeleteClientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteClient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.DeleteClientRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.DeleteClientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("DeleteClient"))
              .build();
        }
      }
    }
    return getDeleteClientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.AuthorizeRequest,
      com.microservices.oauth2.grpc.AuthorizeResponse> getAuthorizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authorize",
      requestType = com.microservices.oauth2.grpc.AuthorizeRequest.class,
      responseType = com.microservices.oauth2.grpc.AuthorizeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.AuthorizeRequest,
      com.microservices.oauth2.grpc.AuthorizeResponse> getAuthorizeMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.AuthorizeRequest, com.microservices.oauth2.grpc.AuthorizeResponse> getAuthorizeMethod;
    if ((getAuthorizeMethod = OAuth2ServiceGrpc.getAuthorizeMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getAuthorizeMethod = OAuth2ServiceGrpc.getAuthorizeMethod) == null) {
          OAuth2ServiceGrpc.getAuthorizeMethod = getAuthorizeMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.AuthorizeRequest, com.microservices.oauth2.grpc.AuthorizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Authorize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.AuthorizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.AuthorizeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("Authorize"))
              .build();
        }
      }
    }
    return getAuthorizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.TokenRequest,
      com.microservices.oauth2.grpc.TokenResponse> getTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Token",
      requestType = com.microservices.oauth2.grpc.TokenRequest.class,
      responseType = com.microservices.oauth2.grpc.TokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.TokenRequest,
      com.microservices.oauth2.grpc.TokenResponse> getTokenMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.TokenRequest, com.microservices.oauth2.grpc.TokenResponse> getTokenMethod;
    if ((getTokenMethod = OAuth2ServiceGrpc.getTokenMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getTokenMethod = OAuth2ServiceGrpc.getTokenMethod) == null) {
          OAuth2ServiceGrpc.getTokenMethod = getTokenMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.TokenRequest, com.microservices.oauth2.grpc.TokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Token"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.TokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.TokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("Token"))
              .build();
        }
      }
    }
    return getTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.IntrospectTokenRequest,
      com.microservices.oauth2.grpc.IntrospectTokenResponse> getIntrospectTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "IntrospectToken",
      requestType = com.microservices.oauth2.grpc.IntrospectTokenRequest.class,
      responseType = com.microservices.oauth2.grpc.IntrospectTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.IntrospectTokenRequest,
      com.microservices.oauth2.grpc.IntrospectTokenResponse> getIntrospectTokenMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.IntrospectTokenRequest, com.microservices.oauth2.grpc.IntrospectTokenResponse> getIntrospectTokenMethod;
    if ((getIntrospectTokenMethod = OAuth2ServiceGrpc.getIntrospectTokenMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getIntrospectTokenMethod = OAuth2ServiceGrpc.getIntrospectTokenMethod) == null) {
          OAuth2ServiceGrpc.getIntrospectTokenMethod = getIntrospectTokenMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.IntrospectTokenRequest, com.microservices.oauth2.grpc.IntrospectTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "IntrospectToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.IntrospectTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.IntrospectTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("IntrospectToken"))
              .build();
        }
      }
    }
    return getIntrospectTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.RevokeTokenRequest,
      com.microservices.oauth2.grpc.RevokeTokenResponse> getRevokeTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RevokeToken",
      requestType = com.microservices.oauth2.grpc.RevokeTokenRequest.class,
      responseType = com.microservices.oauth2.grpc.RevokeTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.RevokeTokenRequest,
      com.microservices.oauth2.grpc.RevokeTokenResponse> getRevokeTokenMethod() {
    io.grpc.MethodDescriptor<com.microservices.oauth2.grpc.RevokeTokenRequest, com.microservices.oauth2.grpc.RevokeTokenResponse> getRevokeTokenMethod;
    if ((getRevokeTokenMethod = OAuth2ServiceGrpc.getRevokeTokenMethod) == null) {
      synchronized (OAuth2ServiceGrpc.class) {
        if ((getRevokeTokenMethod = OAuth2ServiceGrpc.getRevokeTokenMethod) == null) {
          OAuth2ServiceGrpc.getRevokeTokenMethod = getRevokeTokenMethod =
              io.grpc.MethodDescriptor.<com.microservices.oauth2.grpc.RevokeTokenRequest, com.microservices.oauth2.grpc.RevokeTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RevokeToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.RevokeTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.oauth2.grpc.RevokeTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OAuth2ServiceMethodDescriptorSupplier("RevokeToken"))
              .build();
        }
      }
    }
    return getRevokeTokenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OAuth2ServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OAuth2ServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OAuth2ServiceStub>() {
        @java.lang.Override
        public OAuth2ServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OAuth2ServiceStub(channel, callOptions);
        }
      };
    return OAuth2ServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OAuth2ServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OAuth2ServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OAuth2ServiceBlockingStub>() {
        @java.lang.Override
        public OAuth2ServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OAuth2ServiceBlockingStub(channel, callOptions);
        }
      };
    return OAuth2ServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OAuth2ServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OAuth2ServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OAuth2ServiceFutureStub>() {
        @java.lang.Override
        public OAuth2ServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OAuth2ServiceFutureStub(channel, callOptions);
        }
      };
    return OAuth2ServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * OAuth2 Service - Authorization Server operations
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Client Management
     * </pre>
     */
    default void registerClient(com.microservices.oauth2.grpc.RegisterClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.RegisterClientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterClientMethod(), responseObserver);
    }

    /**
     */
    default void getClient(com.microservices.oauth2.grpc.GetClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.GetClientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetClientMethod(), responseObserver);
    }

    /**
     */
    default void updateClient(com.microservices.oauth2.grpc.UpdateClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.UpdateClientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateClientMethod(), responseObserver);
    }

    /**
     */
    default void deleteClient(com.microservices.oauth2.grpc.DeleteClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.DeleteClientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteClientMethod(), responseObserver);
    }

    /**
     * <pre>
     * Authorization Flow
     * </pre>
     */
    default void authorize(com.microservices.oauth2.grpc.AuthorizeRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.AuthorizeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAuthorizeMethod(), responseObserver);
    }

    /**
     * <pre>
     * Token Operations
     * </pre>
     */
    default void token(com.microservices.oauth2.grpc.TokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.TokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTokenMethod(), responseObserver);
    }

    /**
     */
    default void introspectToken(com.microservices.oauth2.grpc.IntrospectTokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.IntrospectTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIntrospectTokenMethod(), responseObserver);
    }

    /**
     */
    default void revokeToken(com.microservices.oauth2.grpc.RevokeTokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.RevokeTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRevokeTokenMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service OAuth2Service.
   * <pre>
   * OAuth2 Service - Authorization Server operations
   * </pre>
   */
  public static abstract class OAuth2ServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return OAuth2ServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service OAuth2Service.
   * <pre>
   * OAuth2 Service - Authorization Server operations
   * </pre>
   */
  public static final class OAuth2ServiceStub
      extends io.grpc.stub.AbstractAsyncStub<OAuth2ServiceStub> {
    private OAuth2ServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OAuth2ServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OAuth2ServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Client Management
     * </pre>
     */
    public void registerClient(com.microservices.oauth2.grpc.RegisterClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.RegisterClientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterClientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClient(com.microservices.oauth2.grpc.GetClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.GetClientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetClientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateClient(com.microservices.oauth2.grpc.UpdateClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.UpdateClientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateClientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteClient(com.microservices.oauth2.grpc.DeleteClientRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.DeleteClientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteClientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Authorization Flow
     * </pre>
     */
    public void authorize(com.microservices.oauth2.grpc.AuthorizeRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.AuthorizeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Token Operations
     * </pre>
     */
    public void token(com.microservices.oauth2.grpc.TokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.TokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void introspectToken(com.microservices.oauth2.grpc.IntrospectTokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.IntrospectTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIntrospectTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void revokeToken(com.microservices.oauth2.grpc.RevokeTokenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.RevokeTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRevokeTokenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service OAuth2Service.
   * <pre>
   * OAuth2 Service - Authorization Server operations
   * </pre>
   */
  public static final class OAuth2ServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<OAuth2ServiceBlockingStub> {
    private OAuth2ServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OAuth2ServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OAuth2ServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Client Management
     * </pre>
     */
    public com.microservices.oauth2.grpc.RegisterClientResponse registerClient(com.microservices.oauth2.grpc.RegisterClientRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterClientMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.oauth2.grpc.GetClientResponse getClient(com.microservices.oauth2.grpc.GetClientRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetClientMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.oauth2.grpc.UpdateClientResponse updateClient(com.microservices.oauth2.grpc.UpdateClientRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateClientMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.oauth2.grpc.DeleteClientResponse deleteClient(com.microservices.oauth2.grpc.DeleteClientRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteClientMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Authorization Flow
     * </pre>
     */
    public com.microservices.oauth2.grpc.AuthorizeResponse authorize(com.microservices.oauth2.grpc.AuthorizeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAuthorizeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Token Operations
     * </pre>
     */
    public com.microservices.oauth2.grpc.TokenResponse token(com.microservices.oauth2.grpc.TokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.oauth2.grpc.IntrospectTokenResponse introspectToken(com.microservices.oauth2.grpc.IntrospectTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIntrospectTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.oauth2.grpc.RevokeTokenResponse revokeToken(com.microservices.oauth2.grpc.RevokeTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRevokeTokenMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service OAuth2Service.
   * <pre>
   * OAuth2 Service - Authorization Server operations
   * </pre>
   */
  public static final class OAuth2ServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<OAuth2ServiceFutureStub> {
    private OAuth2ServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OAuth2ServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OAuth2ServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Client Management
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.RegisterClientResponse> registerClient(
        com.microservices.oauth2.grpc.RegisterClientRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterClientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.GetClientResponse> getClient(
        com.microservices.oauth2.grpc.GetClientRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetClientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.UpdateClientResponse> updateClient(
        com.microservices.oauth2.grpc.UpdateClientRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateClientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.DeleteClientResponse> deleteClient(
        com.microservices.oauth2.grpc.DeleteClientRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteClientMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Authorization Flow
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.AuthorizeResponse> authorize(
        com.microservices.oauth2.grpc.AuthorizeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Token Operations
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.TokenResponse> token(
        com.microservices.oauth2.grpc.TokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.IntrospectTokenResponse> introspectToken(
        com.microservices.oauth2.grpc.IntrospectTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIntrospectTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.oauth2.grpc.RevokeTokenResponse> revokeToken(
        com.microservices.oauth2.grpc.RevokeTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRevokeTokenMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_CLIENT = 0;
  private static final int METHODID_GET_CLIENT = 1;
  private static final int METHODID_UPDATE_CLIENT = 2;
  private static final int METHODID_DELETE_CLIENT = 3;
  private static final int METHODID_AUTHORIZE = 4;
  private static final int METHODID_TOKEN = 5;
  private static final int METHODID_INTROSPECT_TOKEN = 6;
  private static final int METHODID_REVOKE_TOKEN = 7;

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
        case METHODID_REGISTER_CLIENT:
          serviceImpl.registerClient((com.microservices.oauth2.grpc.RegisterClientRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.RegisterClientResponse>) responseObserver);
          break;
        case METHODID_GET_CLIENT:
          serviceImpl.getClient((com.microservices.oauth2.grpc.GetClientRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.GetClientResponse>) responseObserver);
          break;
        case METHODID_UPDATE_CLIENT:
          serviceImpl.updateClient((com.microservices.oauth2.grpc.UpdateClientRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.UpdateClientResponse>) responseObserver);
          break;
        case METHODID_DELETE_CLIENT:
          serviceImpl.deleteClient((com.microservices.oauth2.grpc.DeleteClientRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.DeleteClientResponse>) responseObserver);
          break;
        case METHODID_AUTHORIZE:
          serviceImpl.authorize((com.microservices.oauth2.grpc.AuthorizeRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.AuthorizeResponse>) responseObserver);
          break;
        case METHODID_TOKEN:
          serviceImpl.token((com.microservices.oauth2.grpc.TokenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.TokenResponse>) responseObserver);
          break;
        case METHODID_INTROSPECT_TOKEN:
          serviceImpl.introspectToken((com.microservices.oauth2.grpc.IntrospectTokenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.IntrospectTokenResponse>) responseObserver);
          break;
        case METHODID_REVOKE_TOKEN:
          serviceImpl.revokeToken((com.microservices.oauth2.grpc.RevokeTokenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.oauth2.grpc.RevokeTokenResponse>) responseObserver);
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
          getRegisterClientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.RegisterClientRequest,
              com.microservices.oauth2.grpc.RegisterClientResponse>(
                service, METHODID_REGISTER_CLIENT)))
        .addMethod(
          getGetClientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.GetClientRequest,
              com.microservices.oauth2.grpc.GetClientResponse>(
                service, METHODID_GET_CLIENT)))
        .addMethod(
          getUpdateClientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.UpdateClientRequest,
              com.microservices.oauth2.grpc.UpdateClientResponse>(
                service, METHODID_UPDATE_CLIENT)))
        .addMethod(
          getDeleteClientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.DeleteClientRequest,
              com.microservices.oauth2.grpc.DeleteClientResponse>(
                service, METHODID_DELETE_CLIENT)))
        .addMethod(
          getAuthorizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.AuthorizeRequest,
              com.microservices.oauth2.grpc.AuthorizeResponse>(
                service, METHODID_AUTHORIZE)))
        .addMethod(
          getTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.TokenRequest,
              com.microservices.oauth2.grpc.TokenResponse>(
                service, METHODID_TOKEN)))
        .addMethod(
          getIntrospectTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.IntrospectTokenRequest,
              com.microservices.oauth2.grpc.IntrospectTokenResponse>(
                service, METHODID_INTROSPECT_TOKEN)))
        .addMethod(
          getRevokeTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.oauth2.grpc.RevokeTokenRequest,
              com.microservices.oauth2.grpc.RevokeTokenResponse>(
                service, METHODID_REVOKE_TOKEN)))
        .build();
  }

  private static abstract class OAuth2ServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OAuth2ServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.oauth2.grpc.OAuth2.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OAuth2Service");
    }
  }

  private static final class OAuth2ServiceFileDescriptorSupplier
      extends OAuth2ServiceBaseDescriptorSupplier {
    OAuth2ServiceFileDescriptorSupplier() {}
  }

  private static final class OAuth2ServiceMethodDescriptorSupplier
      extends OAuth2ServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    OAuth2ServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (OAuth2ServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OAuth2ServiceFileDescriptorSupplier())
              .addMethod(getRegisterClientMethod())
              .addMethod(getGetClientMethod())
              .addMethod(getUpdateClientMethod())
              .addMethod(getDeleteClientMethod())
              .addMethod(getAuthorizeMethod())
              .addMethod(getTokenMethod())
              .addMethod(getIntrospectTokenMethod())
              .addMethod(getRevokeTokenMethod())
              .build();
        }
      }
    }
    return result;
  }
}
