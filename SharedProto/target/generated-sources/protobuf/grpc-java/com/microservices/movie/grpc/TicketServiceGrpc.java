package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TicketServiceGrpc {

  private TicketServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.TicketService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GenerateTicketsRequest,
      com.microservices.movie.grpc.GenerateTicketsResponse> getGenerateTicketsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GenerateTickets",
      requestType = com.microservices.movie.grpc.GenerateTicketsRequest.class,
      responseType = com.microservices.movie.grpc.GenerateTicketsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GenerateTicketsRequest,
      com.microservices.movie.grpc.GenerateTicketsResponse> getGenerateTicketsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GenerateTicketsRequest, com.microservices.movie.grpc.GenerateTicketsResponse> getGenerateTicketsMethod;
    if ((getGenerateTicketsMethod = TicketServiceGrpc.getGenerateTicketsMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getGenerateTicketsMethod = TicketServiceGrpc.getGenerateTicketsMethod) == null) {
          TicketServiceGrpc.getGenerateTicketsMethod = getGenerateTicketsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GenerateTicketsRequest, com.microservices.movie.grpc.GenerateTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GenerateTickets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GenerateTicketsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GenerateTicketsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("GenerateTickets"))
              .build();
        }
      }
    }
    return getGenerateTicketsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetTicketRequest,
      com.microservices.movie.grpc.TicketResponse> getGetTicketMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTicket",
      requestType = com.microservices.movie.grpc.GetTicketRequest.class,
      responseType = com.microservices.movie.grpc.TicketResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetTicketRequest,
      com.microservices.movie.grpc.TicketResponse> getGetTicketMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetTicketRequest, com.microservices.movie.grpc.TicketResponse> getGetTicketMethod;
    if ((getGetTicketMethod = TicketServiceGrpc.getGetTicketMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getGetTicketMethod = TicketServiceGrpc.getGetTicketMethod) == null) {
          TicketServiceGrpc.getGetTicketMethod = getGetTicketMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetTicketRequest, com.microservices.movie.grpc.TicketResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTicket"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetTicketRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.TicketResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("GetTicket"))
              .build();
        }
      }
    }
    return getGetTicketMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.VerifyTicketRequest,
      com.microservices.movie.grpc.VerifyTicketResponse> getVerifyTicketMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyTicket",
      requestType = com.microservices.movie.grpc.VerifyTicketRequest.class,
      responseType = com.microservices.movie.grpc.VerifyTicketResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.VerifyTicketRequest,
      com.microservices.movie.grpc.VerifyTicketResponse> getVerifyTicketMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.VerifyTicketRequest, com.microservices.movie.grpc.VerifyTicketResponse> getVerifyTicketMethod;
    if ((getVerifyTicketMethod = TicketServiceGrpc.getVerifyTicketMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getVerifyTicketMethod = TicketServiceGrpc.getVerifyTicketMethod) == null) {
          TicketServiceGrpc.getVerifyTicketMethod = getVerifyTicketMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.VerifyTicketRequest, com.microservices.movie.grpc.VerifyTicketResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyTicket"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.VerifyTicketRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.VerifyTicketResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("VerifyTicket"))
              .build();
        }
      }
    }
    return getVerifyTicketMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListTicketsByBookingRequest,
      com.microservices.movie.grpc.ListTicketsResponse> getListTicketsByBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListTicketsByBooking",
      requestType = com.microservices.movie.grpc.ListTicketsByBookingRequest.class,
      responseType = com.microservices.movie.grpc.ListTicketsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListTicketsByBookingRequest,
      com.microservices.movie.grpc.ListTicketsResponse> getListTicketsByBookingMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListTicketsByBookingRequest, com.microservices.movie.grpc.ListTicketsResponse> getListTicketsByBookingMethod;
    if ((getListTicketsByBookingMethod = TicketServiceGrpc.getListTicketsByBookingMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getListTicketsByBookingMethod = TicketServiceGrpc.getListTicketsByBookingMethod) == null) {
          TicketServiceGrpc.getListTicketsByBookingMethod = getListTicketsByBookingMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListTicketsByBookingRequest, com.microservices.movie.grpc.ListTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListTicketsByBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListTicketsByBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListTicketsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("ListTicketsByBooking"))
              .build();
        }
      }
    }
    return getListTicketsByBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.CancelTicketsRequest,
      com.microservices.movie.grpc.CancelTicketsResponse> getCancelTicketsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelTickets",
      requestType = com.microservices.movie.grpc.CancelTicketsRequest.class,
      responseType = com.microservices.movie.grpc.CancelTicketsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.CancelTicketsRequest,
      com.microservices.movie.grpc.CancelTicketsResponse> getCancelTicketsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.CancelTicketsRequest, com.microservices.movie.grpc.CancelTicketsResponse> getCancelTicketsMethod;
    if ((getCancelTicketsMethod = TicketServiceGrpc.getCancelTicketsMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getCancelTicketsMethod = TicketServiceGrpc.getCancelTicketsMethod) == null) {
          TicketServiceGrpc.getCancelTicketsMethod = getCancelTicketsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.CancelTicketsRequest, com.microservices.movie.grpc.CancelTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelTickets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CancelTicketsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CancelTicketsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("CancelTickets"))
              .build();
        }
      }
    }
    return getCancelTicketsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TicketServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TicketServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TicketServiceStub>() {
        @java.lang.Override
        public TicketServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TicketServiceStub(channel, callOptions);
        }
      };
    return TicketServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TicketServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TicketServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TicketServiceBlockingStub>() {
        @java.lang.Override
        public TicketServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TicketServiceBlockingStub(channel, callOptions);
        }
      };
    return TicketServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TicketServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TicketServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TicketServiceFutureStub>() {
        @java.lang.Override
        public TicketServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TicketServiceFutureStub(channel, callOptions);
        }
      };
    return TicketServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void generateTickets(com.microservices.movie.grpc.GenerateTicketsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GenerateTicketsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGenerateTicketsMethod(), responseObserver);
    }

    /**
     */
    default void getTicket(com.microservices.movie.grpc.GetTicketRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TicketResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTicketMethod(), responseObserver);
    }

    /**
     */
    default void verifyTicket(com.microservices.movie.grpc.VerifyTicketRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.VerifyTicketResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyTicketMethod(), responseObserver);
    }

    /**
     */
    default void listTicketsByBooking(com.microservices.movie.grpc.ListTicketsByBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListTicketsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListTicketsByBookingMethod(), responseObserver);
    }

    /**
     */
    default void cancelTickets(com.microservices.movie.grpc.CancelTicketsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.CancelTicketsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelTicketsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TicketService.
   */
  public static abstract class TicketServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TicketServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TicketService.
   */
  public static final class TicketServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TicketServiceStub> {
    private TicketServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TicketServiceStub(channel, callOptions);
    }

    /**
     */
    public void generateTickets(com.microservices.movie.grpc.GenerateTicketsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GenerateTicketsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGenerateTicketsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTicket(com.microservices.movie.grpc.GetTicketRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TicketResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTicketMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void verifyTicket(com.microservices.movie.grpc.VerifyTicketRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.VerifyTicketResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyTicketMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listTicketsByBooking(com.microservices.movie.grpc.ListTicketsByBookingRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListTicketsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListTicketsByBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelTickets(com.microservices.movie.grpc.CancelTicketsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.CancelTicketsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelTicketsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TicketService.
   */
  public static final class TicketServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TicketServiceBlockingStub> {
    private TicketServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TicketServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.GenerateTicketsResponse generateTickets(com.microservices.movie.grpc.GenerateTicketsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateTicketsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.TicketResponse getTicket(com.microservices.movie.grpc.GetTicketRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTicketMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.VerifyTicketResponse verifyTicket(com.microservices.movie.grpc.VerifyTicketRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyTicketMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListTicketsResponse listTicketsByBooking(com.microservices.movie.grpc.ListTicketsByBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListTicketsByBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.CancelTicketsResponse cancelTickets(com.microservices.movie.grpc.CancelTicketsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelTicketsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TicketService.
   */
  public static final class TicketServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TicketServiceFutureStub> {
    private TicketServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TicketServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.GenerateTicketsResponse> generateTickets(
        com.microservices.movie.grpc.GenerateTicketsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGenerateTicketsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.TicketResponse> getTicket(
        com.microservices.movie.grpc.GetTicketRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTicketMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.VerifyTicketResponse> verifyTicket(
        com.microservices.movie.grpc.VerifyTicketRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyTicketMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListTicketsResponse> listTicketsByBooking(
        com.microservices.movie.grpc.ListTicketsByBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListTicketsByBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.CancelTicketsResponse> cancelTickets(
        com.microservices.movie.grpc.CancelTicketsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelTicketsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GENERATE_TICKETS = 0;
  private static final int METHODID_GET_TICKET = 1;
  private static final int METHODID_VERIFY_TICKET = 2;
  private static final int METHODID_LIST_TICKETS_BY_BOOKING = 3;
  private static final int METHODID_CANCEL_TICKETS = 4;

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
        case METHODID_GENERATE_TICKETS:
          serviceImpl.generateTickets((com.microservices.movie.grpc.GenerateTicketsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GenerateTicketsResponse>) responseObserver);
          break;
        case METHODID_GET_TICKET:
          serviceImpl.getTicket((com.microservices.movie.grpc.GetTicketRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TicketResponse>) responseObserver);
          break;
        case METHODID_VERIFY_TICKET:
          serviceImpl.verifyTicket((com.microservices.movie.grpc.VerifyTicketRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.VerifyTicketResponse>) responseObserver);
          break;
        case METHODID_LIST_TICKETS_BY_BOOKING:
          serviceImpl.listTicketsByBooking((com.microservices.movie.grpc.ListTicketsByBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListTicketsResponse>) responseObserver);
          break;
        case METHODID_CANCEL_TICKETS:
          serviceImpl.cancelTickets((com.microservices.movie.grpc.CancelTicketsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.CancelTicketsResponse>) responseObserver);
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
          getGenerateTicketsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GenerateTicketsRequest,
              com.microservices.movie.grpc.GenerateTicketsResponse>(
                service, METHODID_GENERATE_TICKETS)))
        .addMethod(
          getGetTicketMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetTicketRequest,
              com.microservices.movie.grpc.TicketResponse>(
                service, METHODID_GET_TICKET)))
        .addMethod(
          getVerifyTicketMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.VerifyTicketRequest,
              com.microservices.movie.grpc.VerifyTicketResponse>(
                service, METHODID_VERIFY_TICKET)))
        .addMethod(
          getListTicketsByBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListTicketsByBookingRequest,
              com.microservices.movie.grpc.ListTicketsResponse>(
                service, METHODID_LIST_TICKETS_BY_BOOKING)))
        .addMethod(
          getCancelTicketsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.CancelTicketsRequest,
              com.microservices.movie.grpc.CancelTicketsResponse>(
                service, METHODID_CANCEL_TICKETS)))
        .build();
  }

  private static abstract class TicketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TicketServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TicketService");
    }
  }

  private static final class TicketServiceFileDescriptorSupplier
      extends TicketServiceBaseDescriptorSupplier {
    TicketServiceFileDescriptorSupplier() {}
  }

  private static final class TicketServiceMethodDescriptorSupplier
      extends TicketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TicketServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TicketServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TicketServiceFileDescriptorSupplier())
              .addMethod(getGenerateTicketsMethod())
              .addMethod(getGetTicketMethod())
              .addMethod(getVerifyTicketMethod())
              .addMethod(getListTicketsByBookingMethod())
              .addMethod(getCancelTicketsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
