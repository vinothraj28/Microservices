package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SeatServiceGrpc {

  private SeatServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.SeatService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.LockSeatsRequest,
      com.microservices.movie.grpc.LockSeatsResponse> getLockSeatsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "LockSeats",
      requestType = com.microservices.movie.grpc.LockSeatsRequest.class,
      responseType = com.microservices.movie.grpc.LockSeatsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.LockSeatsRequest,
      com.microservices.movie.grpc.LockSeatsResponse> getLockSeatsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.LockSeatsRequest, com.microservices.movie.grpc.LockSeatsResponse> getLockSeatsMethod;
    if ((getLockSeatsMethod = SeatServiceGrpc.getLockSeatsMethod) == null) {
      synchronized (SeatServiceGrpc.class) {
        if ((getLockSeatsMethod = SeatServiceGrpc.getLockSeatsMethod) == null) {
          SeatServiceGrpc.getLockSeatsMethod = getLockSeatsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.LockSeatsRequest, com.microservices.movie.grpc.LockSeatsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "LockSeats"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.LockSeatsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.LockSeatsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SeatServiceMethodDescriptorSupplier("LockSeats"))
              .build();
        }
      }
    }
    return getLockSeatsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.UnlockSeatsRequest,
      com.microservices.movie.grpc.UnlockSeatsResponse> getUnlockSeatsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UnlockSeats",
      requestType = com.microservices.movie.grpc.UnlockSeatsRequest.class,
      responseType = com.microservices.movie.grpc.UnlockSeatsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.UnlockSeatsRequest,
      com.microservices.movie.grpc.UnlockSeatsResponse> getUnlockSeatsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.UnlockSeatsRequest, com.microservices.movie.grpc.UnlockSeatsResponse> getUnlockSeatsMethod;
    if ((getUnlockSeatsMethod = SeatServiceGrpc.getUnlockSeatsMethod) == null) {
      synchronized (SeatServiceGrpc.class) {
        if ((getUnlockSeatsMethod = SeatServiceGrpc.getUnlockSeatsMethod) == null) {
          SeatServiceGrpc.getUnlockSeatsMethod = getUnlockSeatsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.UnlockSeatsRequest, com.microservices.movie.grpc.UnlockSeatsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UnlockSeats"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UnlockSeatsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UnlockSeatsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SeatServiceMethodDescriptorSupplier("UnlockSeats"))
              .build();
        }
      }
    }
    return getUnlockSeatsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetSeatStatusRequest,
      com.microservices.movie.grpc.GetSeatStatusResponse> getGetSeatStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSeatStatus",
      requestType = com.microservices.movie.grpc.GetSeatStatusRequest.class,
      responseType = com.microservices.movie.grpc.GetSeatStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetSeatStatusRequest,
      com.microservices.movie.grpc.GetSeatStatusResponse> getGetSeatStatusMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetSeatStatusRequest, com.microservices.movie.grpc.GetSeatStatusResponse> getGetSeatStatusMethod;
    if ((getGetSeatStatusMethod = SeatServiceGrpc.getGetSeatStatusMethod) == null) {
      synchronized (SeatServiceGrpc.class) {
        if ((getGetSeatStatusMethod = SeatServiceGrpc.getGetSeatStatusMethod) == null) {
          SeatServiceGrpc.getGetSeatStatusMethod = getGetSeatStatusMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetSeatStatusRequest, com.microservices.movie.grpc.GetSeatStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSeatStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetSeatStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetSeatStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SeatServiceMethodDescriptorSupplier("GetSeatStatus"))
              .build();
        }
      }
    }
    return getGetSeatStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SeatServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SeatServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SeatServiceStub>() {
        @java.lang.Override
        public SeatServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SeatServiceStub(channel, callOptions);
        }
      };
    return SeatServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SeatServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SeatServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SeatServiceBlockingStub>() {
        @java.lang.Override
        public SeatServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SeatServiceBlockingStub(channel, callOptions);
        }
      };
    return SeatServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SeatServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SeatServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SeatServiceFutureStub>() {
        @java.lang.Override
        public SeatServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SeatServiceFutureStub(channel, callOptions);
        }
      };
    return SeatServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void lockSeats(com.microservices.movie.grpc.LockSeatsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.LockSeatsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLockSeatsMethod(), responseObserver);
    }

    /**
     */
    default void unlockSeats(com.microservices.movie.grpc.UnlockSeatsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.UnlockSeatsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnlockSeatsMethod(), responseObserver);
    }

    /**
     */
    default void getSeatStatus(com.microservices.movie.grpc.GetSeatStatusRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GetSeatStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSeatStatusMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SeatService.
   */
  public static abstract class SeatServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SeatServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SeatService.
   */
  public static final class SeatServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SeatServiceStub> {
    private SeatServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SeatServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SeatServiceStub(channel, callOptions);
    }

    /**
     */
    public void lockSeats(com.microservices.movie.grpc.LockSeatsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.LockSeatsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLockSeatsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unlockSeats(com.microservices.movie.grpc.UnlockSeatsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.UnlockSeatsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnlockSeatsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSeatStatus(com.microservices.movie.grpc.GetSeatStatusRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GetSeatStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSeatStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SeatService.
   */
  public static final class SeatServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SeatServiceBlockingStub> {
    private SeatServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SeatServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SeatServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.LockSeatsResponse lockSeats(com.microservices.movie.grpc.LockSeatsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLockSeatsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.UnlockSeatsResponse unlockSeats(com.microservices.movie.grpc.UnlockSeatsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnlockSeatsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.GetSeatStatusResponse getSeatStatus(com.microservices.movie.grpc.GetSeatStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSeatStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SeatService.
   */
  public static final class SeatServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SeatServiceFutureStub> {
    private SeatServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SeatServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SeatServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.LockSeatsResponse> lockSeats(
        com.microservices.movie.grpc.LockSeatsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLockSeatsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.UnlockSeatsResponse> unlockSeats(
        com.microservices.movie.grpc.UnlockSeatsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnlockSeatsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.GetSeatStatusResponse> getSeatStatus(
        com.microservices.movie.grpc.GetSeatStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSeatStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOCK_SEATS = 0;
  private static final int METHODID_UNLOCK_SEATS = 1;
  private static final int METHODID_GET_SEAT_STATUS = 2;

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
        case METHODID_LOCK_SEATS:
          serviceImpl.lockSeats((com.microservices.movie.grpc.LockSeatsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.LockSeatsResponse>) responseObserver);
          break;
        case METHODID_UNLOCK_SEATS:
          serviceImpl.unlockSeats((com.microservices.movie.grpc.UnlockSeatsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.UnlockSeatsResponse>) responseObserver);
          break;
        case METHODID_GET_SEAT_STATUS:
          serviceImpl.getSeatStatus((com.microservices.movie.grpc.GetSeatStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GetSeatStatusResponse>) responseObserver);
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
          getLockSeatsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.LockSeatsRequest,
              com.microservices.movie.grpc.LockSeatsResponse>(
                service, METHODID_LOCK_SEATS)))
        .addMethod(
          getUnlockSeatsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.UnlockSeatsRequest,
              com.microservices.movie.grpc.UnlockSeatsResponse>(
                service, METHODID_UNLOCK_SEATS)))
        .addMethod(
          getGetSeatStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetSeatStatusRequest,
              com.microservices.movie.grpc.GetSeatStatusResponse>(
                service, METHODID_GET_SEAT_STATUS)))
        .build();
  }

  private static abstract class SeatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SeatServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SeatService");
    }
  }

  private static final class SeatServiceFileDescriptorSupplier
      extends SeatServiceBaseDescriptorSupplier {
    SeatServiceFileDescriptorSupplier() {}
  }

  private static final class SeatServiceMethodDescriptorSupplier
      extends SeatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SeatServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SeatServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SeatServiceFileDescriptorSupplier())
              .addMethod(getLockSeatsMethod())
              .addMethod(getUnlockSeatsMethod())
              .addMethod(getGetSeatStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
