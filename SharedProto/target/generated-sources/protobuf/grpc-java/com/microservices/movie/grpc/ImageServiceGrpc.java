package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ImageServiceGrpc {

  private ImageServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.ImageService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.UploadImageRequest,
      com.microservices.movie.grpc.UploadImageResponse> getUploadImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UploadImage",
      requestType = com.microservices.movie.grpc.UploadImageRequest.class,
      responseType = com.microservices.movie.grpc.UploadImageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.UploadImageRequest,
      com.microservices.movie.grpc.UploadImageResponse> getUploadImageMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.UploadImageRequest, com.microservices.movie.grpc.UploadImageResponse> getUploadImageMethod;
    if ((getUploadImageMethod = ImageServiceGrpc.getUploadImageMethod) == null) {
      synchronized (ImageServiceGrpc.class) {
        if ((getUploadImageMethod = ImageServiceGrpc.getUploadImageMethod) == null) {
          ImageServiceGrpc.getUploadImageMethod = getUploadImageMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.UploadImageRequest, com.microservices.movie.grpc.UploadImageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UploadImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UploadImageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UploadImageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ImageServiceMethodDescriptorSupplier("UploadImage"))
              .build();
        }
      }
    }
    return getUploadImageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetImageRequest,
      com.microservices.movie.grpc.GetImageResponse> getGetImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetImage",
      requestType = com.microservices.movie.grpc.GetImageRequest.class,
      responseType = com.microservices.movie.grpc.GetImageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetImageRequest,
      com.microservices.movie.grpc.GetImageResponse> getGetImageMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetImageRequest, com.microservices.movie.grpc.GetImageResponse> getGetImageMethod;
    if ((getGetImageMethod = ImageServiceGrpc.getGetImageMethod) == null) {
      synchronized (ImageServiceGrpc.class) {
        if ((getGetImageMethod = ImageServiceGrpc.getGetImageMethod) == null) {
          ImageServiceGrpc.getGetImageMethod = getGetImageMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetImageRequest, com.microservices.movie.grpc.GetImageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetImageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetImageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ImageServiceMethodDescriptorSupplier("GetImage"))
              .build();
        }
      }
    }
    return getGetImageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.DeleteImageRequest,
      com.microservices.movie.grpc.DeleteImageResponse> getDeleteImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteImage",
      requestType = com.microservices.movie.grpc.DeleteImageRequest.class,
      responseType = com.microservices.movie.grpc.DeleteImageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.DeleteImageRequest,
      com.microservices.movie.grpc.DeleteImageResponse> getDeleteImageMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.DeleteImageRequest, com.microservices.movie.grpc.DeleteImageResponse> getDeleteImageMethod;
    if ((getDeleteImageMethod = ImageServiceGrpc.getDeleteImageMethod) == null) {
      synchronized (ImageServiceGrpc.class) {
        if ((getDeleteImageMethod = ImageServiceGrpc.getDeleteImageMethod) == null) {
          ImageServiceGrpc.getDeleteImageMethod = getDeleteImageMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.DeleteImageRequest, com.microservices.movie.grpc.DeleteImageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.DeleteImageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.DeleteImageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ImageServiceMethodDescriptorSupplier("DeleteImage"))
              .build();
        }
      }
    }
    return getDeleteImageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ImageServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageServiceStub>() {
        @java.lang.Override
        public ImageServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageServiceStub(channel, callOptions);
        }
      };
    return ImageServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ImageServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageServiceBlockingStub>() {
        @java.lang.Override
        public ImageServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageServiceBlockingStub(channel, callOptions);
        }
      };
    return ImageServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ImageServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageServiceFutureStub>() {
        @java.lang.Override
        public ImageServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageServiceFutureStub(channel, callOptions);
        }
      };
    return ImageServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void uploadImage(com.microservices.movie.grpc.UploadImageRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.UploadImageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUploadImageMethod(), responseObserver);
    }

    /**
     */
    default void getImage(com.microservices.movie.grpc.GetImageRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GetImageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetImageMethod(), responseObserver);
    }

    /**
     */
    default void deleteImage(com.microservices.movie.grpc.DeleteImageRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.DeleteImageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteImageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ImageService.
   */
  public static abstract class ImageServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ImageServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ImageService.
   */
  public static final class ImageServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ImageServiceStub> {
    private ImageServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageServiceStub(channel, callOptions);
    }

    /**
     */
    public void uploadImage(com.microservices.movie.grpc.UploadImageRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.UploadImageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUploadImageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getImage(com.microservices.movie.grpc.GetImageRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GetImageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetImageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteImage(com.microservices.movie.grpc.DeleteImageRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.DeleteImageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteImageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ImageService.
   */
  public static final class ImageServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ImageServiceBlockingStub> {
    private ImageServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.UploadImageResponse uploadImage(com.microservices.movie.grpc.UploadImageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUploadImageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.GetImageResponse getImage(com.microservices.movie.grpc.GetImageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetImageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.DeleteImageResponse deleteImage(com.microservices.movie.grpc.DeleteImageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteImageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ImageService.
   */
  public static final class ImageServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ImageServiceFutureStub> {
    private ImageServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.UploadImageResponse> uploadImage(
        com.microservices.movie.grpc.UploadImageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUploadImageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.GetImageResponse> getImage(
        com.microservices.movie.grpc.GetImageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetImageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.DeleteImageResponse> deleteImage(
        com.microservices.movie.grpc.DeleteImageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteImageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_UPLOAD_IMAGE = 0;
  private static final int METHODID_GET_IMAGE = 1;
  private static final int METHODID_DELETE_IMAGE = 2;

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
        case METHODID_UPLOAD_IMAGE:
          serviceImpl.uploadImage((com.microservices.movie.grpc.UploadImageRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.UploadImageResponse>) responseObserver);
          break;
        case METHODID_GET_IMAGE:
          serviceImpl.getImage((com.microservices.movie.grpc.GetImageRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.GetImageResponse>) responseObserver);
          break;
        case METHODID_DELETE_IMAGE:
          serviceImpl.deleteImage((com.microservices.movie.grpc.DeleteImageRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.DeleteImageResponse>) responseObserver);
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
          getUploadImageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.UploadImageRequest,
              com.microservices.movie.grpc.UploadImageResponse>(
                service, METHODID_UPLOAD_IMAGE)))
        .addMethod(
          getGetImageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetImageRequest,
              com.microservices.movie.grpc.GetImageResponse>(
                service, METHODID_GET_IMAGE)))
        .addMethod(
          getDeleteImageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.DeleteImageRequest,
              com.microservices.movie.grpc.DeleteImageResponse>(
                service, METHODID_DELETE_IMAGE)))
        .build();
  }

  private static abstract class ImageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ImageServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ImageService");
    }
  }

  private static final class ImageServiceFileDescriptorSupplier
      extends ImageServiceBaseDescriptorSupplier {
    ImageServiceFileDescriptorSupplier() {}
  }

  private static final class ImageServiceMethodDescriptorSupplier
      extends ImageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ImageServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ImageServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ImageServiceFileDescriptorSupplier())
              .addMethod(getUploadImageMethod())
              .addMethod(getGetImageMethod())
              .addMethod(getDeleteImageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
