package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PaymentServiceGrpc {

  private PaymentServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.PaymentService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.InitiatePaymentRequest,
      com.microservices.movie.grpc.PaymentResponse> getInitiatePaymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InitiatePayment",
      requestType = com.microservices.movie.grpc.InitiatePaymentRequest.class,
      responseType = com.microservices.movie.grpc.PaymentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.InitiatePaymentRequest,
      com.microservices.movie.grpc.PaymentResponse> getInitiatePaymentMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.InitiatePaymentRequest, com.microservices.movie.grpc.PaymentResponse> getInitiatePaymentMethod;
    if ((getInitiatePaymentMethod = PaymentServiceGrpc.getInitiatePaymentMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getInitiatePaymentMethod = PaymentServiceGrpc.getInitiatePaymentMethod) == null) {
          PaymentServiceGrpc.getInitiatePaymentMethod = getInitiatePaymentMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.InitiatePaymentRequest, com.microservices.movie.grpc.PaymentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InitiatePayment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.InitiatePaymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.PaymentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("InitiatePayment"))
              .build();
        }
      }
    }
    return getInitiatePaymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.VerifyPaymentRequest,
      com.microservices.movie.grpc.PaymentResponse> getVerifyPaymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyPayment",
      requestType = com.microservices.movie.grpc.VerifyPaymentRequest.class,
      responseType = com.microservices.movie.grpc.PaymentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.VerifyPaymentRequest,
      com.microservices.movie.grpc.PaymentResponse> getVerifyPaymentMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.VerifyPaymentRequest, com.microservices.movie.grpc.PaymentResponse> getVerifyPaymentMethod;
    if ((getVerifyPaymentMethod = PaymentServiceGrpc.getVerifyPaymentMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getVerifyPaymentMethod = PaymentServiceGrpc.getVerifyPaymentMethod) == null) {
          PaymentServiceGrpc.getVerifyPaymentMethod = getVerifyPaymentMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.VerifyPaymentRequest, com.microservices.movie.grpc.PaymentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyPayment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.VerifyPaymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.PaymentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("VerifyPayment"))
              .build();
        }
      }
    }
    return getVerifyPaymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ProcessRefundRequest,
      com.microservices.movie.grpc.RefundResponse> getProcessRefundMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ProcessRefund",
      requestType = com.microservices.movie.grpc.ProcessRefundRequest.class,
      responseType = com.microservices.movie.grpc.RefundResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ProcessRefundRequest,
      com.microservices.movie.grpc.RefundResponse> getProcessRefundMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ProcessRefundRequest, com.microservices.movie.grpc.RefundResponse> getProcessRefundMethod;
    if ((getProcessRefundMethod = PaymentServiceGrpc.getProcessRefundMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getProcessRefundMethod = PaymentServiceGrpc.getProcessRefundMethod) == null) {
          PaymentServiceGrpc.getProcessRefundMethod = getProcessRefundMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ProcessRefundRequest, com.microservices.movie.grpc.RefundResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ProcessRefund"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ProcessRefundRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.RefundResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("ProcessRefund"))
              .build();
        }
      }
    }
    return getProcessRefundMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetPaymentStatusRequest,
      com.microservices.movie.grpc.PaymentResponse> getGetPaymentStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPaymentStatus",
      requestType = com.microservices.movie.grpc.GetPaymentStatusRequest.class,
      responseType = com.microservices.movie.grpc.PaymentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetPaymentStatusRequest,
      com.microservices.movie.grpc.PaymentResponse> getGetPaymentStatusMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetPaymentStatusRequest, com.microservices.movie.grpc.PaymentResponse> getGetPaymentStatusMethod;
    if ((getGetPaymentStatusMethod = PaymentServiceGrpc.getGetPaymentStatusMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getGetPaymentStatusMethod = PaymentServiceGrpc.getGetPaymentStatusMethod) == null) {
          PaymentServiceGrpc.getGetPaymentStatusMethod = getGetPaymentStatusMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetPaymentStatusRequest, com.microservices.movie.grpc.PaymentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPaymentStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetPaymentStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.PaymentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("GetPaymentStatus"))
              .build();
        }
      }
    }
    return getGetPaymentStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PaymentServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceStub>() {
        @java.lang.Override
        public PaymentServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceStub(channel, callOptions);
        }
      };
    return PaymentServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PaymentServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceBlockingStub>() {
        @java.lang.Override
        public PaymentServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceBlockingStub(channel, callOptions);
        }
      };
    return PaymentServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PaymentServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceFutureStub>() {
        @java.lang.Override
        public PaymentServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceFutureStub(channel, callOptions);
        }
      };
    return PaymentServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void initiatePayment(com.microservices.movie.grpc.InitiatePaymentRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitiatePaymentMethod(), responseObserver);
    }

    /**
     */
    default void verifyPayment(com.microservices.movie.grpc.VerifyPaymentRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyPaymentMethod(), responseObserver);
    }

    /**
     */
    default void processRefund(com.microservices.movie.grpc.ProcessRefundRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.RefundResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProcessRefundMethod(), responseObserver);
    }

    /**
     */
    default void getPaymentStatus(com.microservices.movie.grpc.GetPaymentStatusRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPaymentStatusMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PaymentService.
   */
  public static abstract class PaymentServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PaymentServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PaymentService.
   */
  public static final class PaymentServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PaymentServiceStub> {
    private PaymentServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceStub(channel, callOptions);
    }

    /**
     */
    public void initiatePayment(com.microservices.movie.grpc.InitiatePaymentRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitiatePaymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void verifyPayment(com.microservices.movie.grpc.VerifyPaymentRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyPaymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void processRefund(com.microservices.movie.grpc.ProcessRefundRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.RefundResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProcessRefundMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPaymentStatus(com.microservices.movie.grpc.GetPaymentStatusRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPaymentStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PaymentService.
   */
  public static final class PaymentServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PaymentServiceBlockingStub> {
    private PaymentServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.PaymentResponse initiatePayment(com.microservices.movie.grpc.InitiatePaymentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitiatePaymentMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.PaymentResponse verifyPayment(com.microservices.movie.grpc.VerifyPaymentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyPaymentMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.RefundResponse processRefund(com.microservices.movie.grpc.ProcessRefundRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProcessRefundMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.PaymentResponse getPaymentStatus(com.microservices.movie.grpc.GetPaymentStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPaymentStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PaymentService.
   */
  public static final class PaymentServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PaymentServiceFutureStub> {
    private PaymentServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.PaymentResponse> initiatePayment(
        com.microservices.movie.grpc.InitiatePaymentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitiatePaymentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.PaymentResponse> verifyPayment(
        com.microservices.movie.grpc.VerifyPaymentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyPaymentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.RefundResponse> processRefund(
        com.microservices.movie.grpc.ProcessRefundRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProcessRefundMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.PaymentResponse> getPaymentStatus(
        com.microservices.movie.grpc.GetPaymentStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPaymentStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INITIATE_PAYMENT = 0;
  private static final int METHODID_VERIFY_PAYMENT = 1;
  private static final int METHODID_PROCESS_REFUND = 2;
  private static final int METHODID_GET_PAYMENT_STATUS = 3;

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
        case METHODID_INITIATE_PAYMENT:
          serviceImpl.initiatePayment((com.microservices.movie.grpc.InitiatePaymentRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse>) responseObserver);
          break;
        case METHODID_VERIFY_PAYMENT:
          serviceImpl.verifyPayment((com.microservices.movie.grpc.VerifyPaymentRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse>) responseObserver);
          break;
        case METHODID_PROCESS_REFUND:
          serviceImpl.processRefund((com.microservices.movie.grpc.ProcessRefundRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.RefundResponse>) responseObserver);
          break;
        case METHODID_GET_PAYMENT_STATUS:
          serviceImpl.getPaymentStatus((com.microservices.movie.grpc.GetPaymentStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.PaymentResponse>) responseObserver);
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
          getInitiatePaymentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.InitiatePaymentRequest,
              com.microservices.movie.grpc.PaymentResponse>(
                service, METHODID_INITIATE_PAYMENT)))
        .addMethod(
          getVerifyPaymentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.VerifyPaymentRequest,
              com.microservices.movie.grpc.PaymentResponse>(
                service, METHODID_VERIFY_PAYMENT)))
        .addMethod(
          getProcessRefundMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ProcessRefundRequest,
              com.microservices.movie.grpc.RefundResponse>(
                service, METHODID_PROCESS_REFUND)))
        .addMethod(
          getGetPaymentStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetPaymentStatusRequest,
              com.microservices.movie.grpc.PaymentResponse>(
                service, METHODID_GET_PAYMENT_STATUS)))
        .build();
  }

  private static abstract class PaymentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PaymentServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PaymentService");
    }
  }

  private static final class PaymentServiceFileDescriptorSupplier
      extends PaymentServiceBaseDescriptorSupplier {
    PaymentServiceFileDescriptorSupplier() {}
  }

  private static final class PaymentServiceMethodDescriptorSupplier
      extends PaymentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PaymentServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (PaymentServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PaymentServiceFileDescriptorSupplier())
              .addMethod(getInitiatePaymentMethod())
              .addMethod(getVerifyPaymentMethod())
              .addMethod(getProcessRefundMethod())
              .addMethod(getGetPaymentStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
