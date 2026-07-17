package com.microservices.profile.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: MFA.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MfaServiceGrpc {

  private MfaServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "user.MfaService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaSetupRequest,
      com.microservices.profile.grpc.MfaSetupResponse> getMfaSetupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MfaSetup",
      requestType = com.microservices.profile.grpc.MfaSetupRequest.class,
      responseType = com.microservices.profile.grpc.MfaSetupResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaSetupRequest,
      com.microservices.profile.grpc.MfaSetupResponse> getMfaSetupMethod() {
    io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaSetupRequest, com.microservices.profile.grpc.MfaSetupResponse> getMfaSetupMethod;
    if ((getMfaSetupMethod = MfaServiceGrpc.getMfaSetupMethod) == null) {
      synchronized (MfaServiceGrpc.class) {
        if ((getMfaSetupMethod = MfaServiceGrpc.getMfaSetupMethod) == null) {
          MfaServiceGrpc.getMfaSetupMethod = getMfaSetupMethod =
              io.grpc.MethodDescriptor.<com.microservices.profile.grpc.MfaSetupRequest, com.microservices.profile.grpc.MfaSetupResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MfaSetup"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.MfaSetupRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.MfaSetupResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MfaServiceMethodDescriptorSupplier("MfaSetup"))
              .build();
        }
      }
    }
    return getMfaSetupMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaConfirmRequest,
      com.microservices.profile.grpc.MfaConfirmResponse> getMfaConfirmMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MfaConfirm",
      requestType = com.microservices.profile.grpc.MfaConfirmRequest.class,
      responseType = com.microservices.profile.grpc.MfaConfirmResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaConfirmRequest,
      com.microservices.profile.grpc.MfaConfirmResponse> getMfaConfirmMethod() {
    io.grpc.MethodDescriptor<com.microservices.profile.grpc.MfaConfirmRequest, com.microservices.profile.grpc.MfaConfirmResponse> getMfaConfirmMethod;
    if ((getMfaConfirmMethod = MfaServiceGrpc.getMfaConfirmMethod) == null) {
      synchronized (MfaServiceGrpc.class) {
        if ((getMfaConfirmMethod = MfaServiceGrpc.getMfaConfirmMethod) == null) {
          MfaServiceGrpc.getMfaConfirmMethod = getMfaConfirmMethod =
              io.grpc.MethodDescriptor.<com.microservices.profile.grpc.MfaConfirmRequest, com.microservices.profile.grpc.MfaConfirmResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MfaConfirm"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.MfaConfirmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.profile.grpc.MfaConfirmResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MfaServiceMethodDescriptorSupplier("MfaConfirm"))
              .build();
        }
      }
    }
    return getMfaConfirmMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MfaServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MfaServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MfaServiceStub>() {
        @java.lang.Override
        public MfaServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MfaServiceStub(channel, callOptions);
        }
      };
    return MfaServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MfaServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MfaServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MfaServiceBlockingStub>() {
        @java.lang.Override
        public MfaServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MfaServiceBlockingStub(channel, callOptions);
        }
      };
    return MfaServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MfaServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MfaServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MfaServiceFutureStub>() {
        @java.lang.Override
        public MfaServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MfaServiceFutureStub(channel, callOptions);
        }
      };
    return MfaServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void mfaSetup(com.microservices.profile.grpc.MfaSetupRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaSetupResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMfaSetupMethod(), responseObserver);
    }

    /**
     */
    default void mfaConfirm(com.microservices.profile.grpc.MfaConfirmRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaConfirmResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMfaConfirmMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MfaService.
   */
  public static abstract class MfaServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MfaServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MfaService.
   */
  public static final class MfaServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MfaServiceStub> {
    private MfaServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MfaServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MfaServiceStub(channel, callOptions);
    }

    /**
     */
    public void mfaSetup(com.microservices.profile.grpc.MfaSetupRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaSetupResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMfaSetupMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void mfaConfirm(com.microservices.profile.grpc.MfaConfirmRequest request,
        io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaConfirmResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMfaConfirmMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MfaService.
   */
  public static final class MfaServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MfaServiceBlockingStub> {
    private MfaServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MfaServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MfaServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.profile.grpc.MfaSetupResponse mfaSetup(com.microservices.profile.grpc.MfaSetupRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMfaSetupMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.profile.grpc.MfaConfirmResponse mfaConfirm(com.microservices.profile.grpc.MfaConfirmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMfaConfirmMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MfaService.
   */
  public static final class MfaServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MfaServiceFutureStub> {
    private MfaServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MfaServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MfaServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.profile.grpc.MfaSetupResponse> mfaSetup(
        com.microservices.profile.grpc.MfaSetupRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMfaSetupMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.profile.grpc.MfaConfirmResponse> mfaConfirm(
        com.microservices.profile.grpc.MfaConfirmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMfaConfirmMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_MFA_SETUP = 0;
  private static final int METHODID_MFA_CONFIRM = 1;

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
        case METHODID_MFA_SETUP:
          serviceImpl.mfaSetup((com.microservices.profile.grpc.MfaSetupRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaSetupResponse>) responseObserver);
          break;
        case METHODID_MFA_CONFIRM:
          serviceImpl.mfaConfirm((com.microservices.profile.grpc.MfaConfirmRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.profile.grpc.MfaConfirmResponse>) responseObserver);
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
          getMfaSetupMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.profile.grpc.MfaSetupRequest,
              com.microservices.profile.grpc.MfaSetupResponse>(
                service, METHODID_MFA_SETUP)))
        .addMethod(
          getMfaConfirmMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.profile.grpc.MfaConfirmRequest,
              com.microservices.profile.grpc.MfaConfirmResponse>(
                service, METHODID_MFA_CONFIRM)))
        .build();
  }

  private static abstract class MfaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MfaServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.profile.grpc.MFA.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MfaService");
    }
  }

  private static final class MfaServiceFileDescriptorSupplier
      extends MfaServiceBaseDescriptorSupplier {
    MfaServiceFileDescriptorSupplier() {}
  }

  private static final class MfaServiceMethodDescriptorSupplier
      extends MfaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MfaServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MfaServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MfaServiceFileDescriptorSupplier())
              .addMethod(getMfaSetupMethod())
              .addMethod(getMfaConfirmMethod())
              .build();
        }
      }
    }
    return result;
  }
}
