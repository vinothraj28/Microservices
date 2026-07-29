package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookingServiceGrpc {

  private BookingServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.BookingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateBookingRequest,
      com.microservices.movie.grpc.BookingResponse> getCreateBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateBooking",
      requestType = com.microservices.movie.grpc.CreateBookingRequest.class,
      responseType = com.microservices.movie.grpc.BookingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateBookingRequest,
      com.microservices.movie.grpc.BookingResponse> getCreateBookingMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateBookingRequest, com.microservices.movie.grpc.BookingResponse> getCreateBookingMethod;
    if ((getCreateBookingMethod = BookingServiceGrpc.getCreateBookingMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getCreateBookingMethod = BookingServiceGrpc.getCreateBookingMethod) == null) {
          BookingServiceGrpc.getCreateBookingMethod = getCreateBookingMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.CreateBookingRequest, com.microservices.movie.grpc.BookingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CreateBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.BookingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("CreateBooking"))
              .build();
        }
      }
    }
    return getCreateBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ConfirmBookingRequest,
      com.microservices.movie.grpc.BookingResponse> getConfirmBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ConfirmBooking",
      requestType = com.microservices.movie.grpc.ConfirmBookingRequest.class,
      responseType = com.microservices.movie.grpc.BookingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ConfirmBookingRequest,
      com.microservices.movie.grpc.BookingResponse> getConfirmBookingMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ConfirmBookingRequest, com.microservices.movie.grpc.BookingResponse> getConfirmBookingMethod;
    if ((getConfirmBookingMethod = BookingServiceGrpc.getConfirmBookingMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getConfirmBookingMethod = BookingServiceGrpc.getConfirmBookingMethod) == null) {
          BookingServiceGrpc.getConfirmBookingMethod = getConfirmBookingMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ConfirmBookingRequest, com.microservices.movie.grpc.BookingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ConfirmBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ConfirmBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.BookingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("ConfirmBooking"))
              .build();
        }
      }
    }
    return getConfirmBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.CancelBookingRequest,
      com.microservices.movie.grpc.CancelBookingResponse> getCancelBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelBooking",
      requestType = com.microservices.movie.grpc.CancelBookingRequest.class,
      responseType = com.microservices.movie.grpc.CancelBookingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.CancelBookingRequest,
      com.microservices.movie.grpc.CancelBookingResponse> getCancelBookingMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.CancelBookingRequest, com.microservices.movie.grpc.CancelBookingResponse> getCancelBookingMethod;
    if ((getCancelBookingMethod = BookingServiceGrpc.getCancelBookingMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getCancelBookingMethod = BookingServiceGrpc.getCancelBookingMethod) == null) {
          BookingServiceGrpc.getCancelBookingMethod = getCancelBookingMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.CancelBookingRequest, com.microservices.movie.grpc.CancelBookingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CancelBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CancelBookingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("CancelBooking"))
              .build();
        }
      }
    }
    return getCancelBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetBookingRequest,
      com.microservices.movie.grpc.BookingResponse> getGetBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBooking",
      requestType = com.microservices.movie.grpc.GetBookingRequest.class,
      responseType = com.microservices.movie.grpc.BookingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetBookingRequest,
      com.microservices.movie.grpc.BookingResponse> getGetBookingMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetBookingRequest, com.microservices.movie.grpc.BookingResponse> getGetBookingMethod;
    if ((getGetBookingMethod = BookingServiceGrpc.getGetBookingMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getGetBookingMethod = BookingServiceGrpc.getGetBookingMethod) == null) {
          BookingServiceGrpc.getGetBookingMethod = getGetBookingMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetBookingRequest, com.microservices.movie.grpc.BookingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.BookingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("GetBooking"))
              .build();
        }
      }
    }
    return getGetBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListUserBookingsRequest,
      com.microservices.movie.grpc.ListBookingsResponse> getListUserBookingsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListUserBookings",
      requestType = com.microservices.movie.grpc.ListUserBookingsRequest.class,
      responseType = com.microservices.movie.grpc.ListBookingsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListUserBookingsRequest,
      com.microservices.movie.grpc.ListBookingsResponse> getListUserBookingsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListUserBookingsRequest, com.microservices.movie.grpc.ListBookingsResponse> getListUserBookingsMethod;
    if ((getListUserBookingsMethod = BookingServiceGrpc.getListUserBookingsMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getListUserBookingsMethod = BookingServiceGrpc.getListUserBookingsMethod) == null) {
          BookingServiceGrpc.getListUserBookingsMethod = getListUserBookingsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListUserBookingsRequest, com.microservices.movie.grpc.ListBookingsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListUserBookings"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListUserBookingsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListBookingsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("ListUserBookings"))
              .build();
        }
      }
    }
    return getListUserBookingsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookingServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceStub>() {
        @java.lang.Override
        public BookingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceStub(channel, callOptions);
        }
      };
    return BookingServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceBlockingStub>() {
        @java.lang.Override
        public BookingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceBlockingStub(channel, callOptions);
        }
      };
    return BookingServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceFutureStub>() {
        @java.lang.Override
        public BookingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceFutureStub(channel, callOptions);
        }
      };
    return BookingServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createBooking(com.microservices.movie.grpc.CreateBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateBookingMethod(), responseObserver);
    }

    /**
     */
    default void confirmBooking(com.microservices.movie.grpc.ConfirmBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConfirmBookingMethod(), responseObserver);
    }

    /**
     */
    default void cancelBooking(com.microservices.movie.grpc.CancelBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.CancelBookingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelBookingMethod(), responseObserver);
    }

    /**
     */
    default void getBooking(com.microservices.movie.grpc.GetBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBookingMethod(), responseObserver);
    }

    /**
     */
    default void listUserBookings(com.microservices.movie.grpc.ListUserBookingsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListBookingsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListUserBookingsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookingService.
   */
  public static abstract class BookingServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookingServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookingService.
   */
  public static final class BookingServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BookingServiceStub> {
    private BookingServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceStub(channel, callOptions);
    }

    /**
     */
    public void createBooking(com.microservices.movie.grpc.CreateBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void confirmBooking(com.microservices.movie.grpc.ConfirmBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConfirmBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelBooking(com.microservices.movie.grpc.CancelBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.CancelBookingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBooking(com.microservices.movie.grpc.GetBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listUserBookings(com.microservices.movie.grpc.ListUserBookingsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListBookingsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListUserBookingsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookingService.
   */
  public static final class BookingServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookingServiceBlockingStub> {
    private BookingServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.BookingResponse createBooking(com.microservices.movie.grpc.CreateBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.BookingResponse confirmBooking(com.microservices.movie.grpc.ConfirmBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConfirmBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.CancelBookingResponse cancelBooking(com.microservices.movie.grpc.CancelBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.BookingResponse getBooking(com.microservices.movie.grpc.GetBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListBookingsResponse listUserBookings(com.microservices.movie.grpc.ListUserBookingsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListUserBookingsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookingService.
   */
  public static final class BookingServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookingServiceFutureStub> {
    private BookingServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.BookingResponse> createBooking(
        com.microservices.movie.grpc.CreateBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.BookingResponse> confirmBooking(
        com.microservices.movie.grpc.ConfirmBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConfirmBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.CancelBookingResponse> cancelBooking(
        com.microservices.movie.grpc.CancelBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.BookingResponse> getBooking(
        com.microservices.movie.grpc.GetBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListBookingsResponse> listUserBookings(
        com.microservices.movie.grpc.ListUserBookingsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListUserBookingsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_BOOKING = 0;
  private static final int METHODID_CONFIRM_BOOKING = 1;
  private static final int METHODID_CANCEL_BOOKING = 2;
  private static final int METHODID_GET_BOOKING = 3;
  private static final int METHODID_LIST_USER_BOOKINGS = 4;

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
        case METHODID_CREATE_BOOKING:
          serviceImpl.createBooking((com.microservices.movie.grpc.CreateBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse>) responseObserver);
          break;
        case METHODID_CONFIRM_BOOKING:
          serviceImpl.confirmBooking((com.microservices.movie.grpc.ConfirmBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse>) responseObserver);
          break;
        case METHODID_CANCEL_BOOKING:
          serviceImpl.cancelBooking((com.microservices.movie.grpc.CancelBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.CancelBookingResponse>) responseObserver);
          break;
        case METHODID_GET_BOOKING:
          serviceImpl.getBooking((com.microservices.movie.grpc.GetBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.BookingResponse>) responseObserver);
          break;
        case METHODID_LIST_USER_BOOKINGS:
          serviceImpl.listUserBookings((com.microservices.movie.grpc.ListUserBookingsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListBookingsResponse>) responseObserver);
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
          getCreateBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.CreateBookingRequest,
              com.microservices.movie.grpc.BookingResponse>(
                service, METHODID_CREATE_BOOKING)))
        .addMethod(
          getConfirmBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ConfirmBookingRequest,
              com.microservices.movie.grpc.BookingResponse>(
                service, METHODID_CONFIRM_BOOKING)))
        .addMethod(
          getCancelBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.CancelBookingRequest,
              com.microservices.movie.grpc.CancelBookingResponse>(
                service, METHODID_CANCEL_BOOKING)))
        .addMethod(
          getGetBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetBookingRequest,
              com.microservices.movie.grpc.BookingResponse>(
                service, METHODID_GET_BOOKING)))
        .addMethod(
          getListUserBookingsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListUserBookingsRequest,
              com.microservices.movie.grpc.ListBookingsResponse>(
                service, METHODID_LIST_USER_BOOKINGS)))
        .build();
  }

  private static abstract class BookingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookingService");
    }
  }

  private static final class BookingServiceFileDescriptorSupplier
      extends BookingServiceBaseDescriptorSupplier {
    BookingServiceFileDescriptorSupplier() {}
  }

  private static final class BookingServiceMethodDescriptorSupplier
      extends BookingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BookingServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BookingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookingServiceFileDescriptorSupplier())
              .addMethod(getCreateBookingMethod())
              .addMethod(getConfirmBookingMethod())
              .addMethod(getCancelBookingMethod())
              .addMethod(getGetBookingMethod())
              .addMethod(getListUserBookingsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
