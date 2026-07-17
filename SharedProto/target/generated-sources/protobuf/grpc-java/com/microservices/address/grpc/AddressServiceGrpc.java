package com.microservices.address.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Address.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AddressServiceGrpc {

  private AddressServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "address.AddressService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.address.grpc.AddressRegisterRequest,
      com.microservices.address.grpc.AddressRegisterResponse> getRegisterAddressMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerAddress",
      requestType = com.microservices.address.grpc.AddressRegisterRequest.class,
      responseType = com.microservices.address.grpc.AddressRegisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.address.grpc.AddressRegisterRequest,
      com.microservices.address.grpc.AddressRegisterResponse> getRegisterAddressMethod() {
    io.grpc.MethodDescriptor<com.microservices.address.grpc.AddressRegisterRequest, com.microservices.address.grpc.AddressRegisterResponse> getRegisterAddressMethod;
    if ((getRegisterAddressMethod = AddressServiceGrpc.getRegisterAddressMethod) == null) {
      synchronized (AddressServiceGrpc.class) {
        if ((getRegisterAddressMethod = AddressServiceGrpc.getRegisterAddressMethod) == null) {
          AddressServiceGrpc.getRegisterAddressMethod = getRegisterAddressMethod =
              io.grpc.MethodDescriptor.<com.microservices.address.grpc.AddressRegisterRequest, com.microservices.address.grpc.AddressRegisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.address.grpc.AddressRegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.address.grpc.AddressRegisterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AddressServiceMethodDescriptorSupplier("registerAddress"))
              .build();
        }
      }
    }
    return getRegisterAddressMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.address.grpc.FindAddressRequest,
      com.microservices.address.grpc.AddressRegisterResponse> getFindByAddressIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findByAddressId",
      requestType = com.microservices.address.grpc.FindAddressRequest.class,
      responseType = com.microservices.address.grpc.AddressRegisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.address.grpc.FindAddressRequest,
      com.microservices.address.grpc.AddressRegisterResponse> getFindByAddressIdMethod() {
    io.grpc.MethodDescriptor<com.microservices.address.grpc.FindAddressRequest, com.microservices.address.grpc.AddressRegisterResponse> getFindByAddressIdMethod;
    if ((getFindByAddressIdMethod = AddressServiceGrpc.getFindByAddressIdMethod) == null) {
      synchronized (AddressServiceGrpc.class) {
        if ((getFindByAddressIdMethod = AddressServiceGrpc.getFindByAddressIdMethod) == null) {
          AddressServiceGrpc.getFindByAddressIdMethod = getFindByAddressIdMethod =
              io.grpc.MethodDescriptor.<com.microservices.address.grpc.FindAddressRequest, com.microservices.address.grpc.AddressRegisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findByAddressId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.address.grpc.FindAddressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.address.grpc.AddressRegisterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AddressServiceMethodDescriptorSupplier("findByAddressId"))
              .build();
        }
      }
    }
    return getFindByAddressIdMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AddressServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AddressServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AddressServiceStub>() {
        @java.lang.Override
        public AddressServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AddressServiceStub(channel, callOptions);
        }
      };
    return AddressServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AddressServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AddressServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AddressServiceBlockingStub>() {
        @java.lang.Override
        public AddressServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AddressServiceBlockingStub(channel, callOptions);
        }
      };
    return AddressServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AddressServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AddressServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AddressServiceFutureStub>() {
        @java.lang.Override
        public AddressServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AddressServiceFutureStub(channel, callOptions);
        }
      };
    return AddressServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void registerAddress(com.microservices.address.grpc.AddressRegisterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.address.grpc.AddressRegisterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterAddressMethod(), responseObserver);
    }

    /**
     */
    default void findByAddressId(com.microservices.address.grpc.FindAddressRequest request,
        io.grpc.stub.StreamObserver<com.microservices.address.grpc.AddressRegisterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindByAddressIdMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AddressService.
   */
  public static abstract class AddressServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AddressServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AddressService.
   */
  public static final class AddressServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AddressServiceStub> {
    private AddressServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AddressServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AddressServiceStub(channel, callOptions);
    }

    /**
     */
    public void registerAddress(com.microservices.address.grpc.AddressRegisterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.address.grpc.AddressRegisterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterAddressMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findByAddressId(com.microservices.address.grpc.FindAddressRequest request,
        io.grpc.stub.StreamObserver<com.microservices.address.grpc.AddressRegisterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindByAddressIdMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AddressService.
   */
  public static final class AddressServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AddressServiceBlockingStub> {
    private AddressServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AddressServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AddressServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.address.grpc.AddressRegisterResponse registerAddress(com.microservices.address.grpc.AddressRegisterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterAddressMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.address.grpc.AddressRegisterResponse findByAddressId(com.microservices.address.grpc.FindAddressRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindByAddressIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AddressService.
   */
  public static final class AddressServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AddressServiceFutureStub> {
    private AddressServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AddressServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AddressServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.address.grpc.AddressRegisterResponse> registerAddress(
        com.microservices.address.grpc.AddressRegisterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterAddressMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.address.grpc.AddressRegisterResponse> findByAddressId(
        com.microservices.address.grpc.FindAddressRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindByAddressIdMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_ADDRESS = 0;
  private static final int METHODID_FIND_BY_ADDRESS_ID = 1;

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
        case METHODID_REGISTER_ADDRESS:
          serviceImpl.registerAddress((com.microservices.address.grpc.AddressRegisterRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.address.grpc.AddressRegisterResponse>) responseObserver);
          break;
        case METHODID_FIND_BY_ADDRESS_ID:
          serviceImpl.findByAddressId((com.microservices.address.grpc.FindAddressRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.address.grpc.AddressRegisterResponse>) responseObserver);
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
          getRegisterAddressMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.address.grpc.AddressRegisterRequest,
              com.microservices.address.grpc.AddressRegisterResponse>(
                service, METHODID_REGISTER_ADDRESS)))
        .addMethod(
          getFindByAddressIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.address.grpc.FindAddressRequest,
              com.microservices.address.grpc.AddressRegisterResponse>(
                service, METHODID_FIND_BY_ADDRESS_ID)))
        .build();
  }

  private static abstract class AddressServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AddressServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.address.grpc.AddressOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AddressService");
    }
  }

  private static final class AddressServiceFileDescriptorSupplier
      extends AddressServiceBaseDescriptorSupplier {
    AddressServiceFileDescriptorSupplier() {}
  }

  private static final class AddressServiceMethodDescriptorSupplier
      extends AddressServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AddressServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (AddressServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AddressServiceFileDescriptorSupplier())
              .addMethod(getRegisterAddressMethod())
              .addMethod(getFindByAddressIdMethod())
              .build();
        }
      }
    }
    return result;
  }
}
