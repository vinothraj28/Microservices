package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TheaterServiceGrpc {

  private TheaterServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.TheaterService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateTheaterRequest,
      com.microservices.movie.grpc.TheaterResponse> getCreateTheaterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateTheater",
      requestType = com.microservices.movie.grpc.CreateTheaterRequest.class,
      responseType = com.microservices.movie.grpc.TheaterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateTheaterRequest,
      com.microservices.movie.grpc.TheaterResponse> getCreateTheaterMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateTheaterRequest, com.microservices.movie.grpc.TheaterResponse> getCreateTheaterMethod;
    if ((getCreateTheaterMethod = TheaterServiceGrpc.getCreateTheaterMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getCreateTheaterMethod = TheaterServiceGrpc.getCreateTheaterMethod) == null) {
          TheaterServiceGrpc.getCreateTheaterMethod = getCreateTheaterMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.CreateTheaterRequest, com.microservices.movie.grpc.TheaterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateTheater"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CreateTheaterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.TheaterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("CreateTheater"))
              .build();
        }
      }
    }
    return getCreateTheaterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateTheaterRequest,
      com.microservices.movie.grpc.TheaterResponse> getUpdateTheaterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateTheater",
      requestType = com.microservices.movie.grpc.UpdateTheaterRequest.class,
      responseType = com.microservices.movie.grpc.TheaterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateTheaterRequest,
      com.microservices.movie.grpc.TheaterResponse> getUpdateTheaterMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateTheaterRequest, com.microservices.movie.grpc.TheaterResponse> getUpdateTheaterMethod;
    if ((getUpdateTheaterMethod = TheaterServiceGrpc.getUpdateTheaterMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getUpdateTheaterMethod = TheaterServiceGrpc.getUpdateTheaterMethod) == null) {
          TheaterServiceGrpc.getUpdateTheaterMethod = getUpdateTheaterMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.UpdateTheaterRequest, com.microservices.movie.grpc.TheaterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateTheater"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UpdateTheaterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.TheaterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("UpdateTheater"))
              .build();
        }
      }
    }
    return getUpdateTheaterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetTheaterRequest,
      com.microservices.movie.grpc.TheaterResponse> getGetTheaterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTheater",
      requestType = com.microservices.movie.grpc.GetTheaterRequest.class,
      responseType = com.microservices.movie.grpc.TheaterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetTheaterRequest,
      com.microservices.movie.grpc.TheaterResponse> getGetTheaterMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetTheaterRequest, com.microservices.movie.grpc.TheaterResponse> getGetTheaterMethod;
    if ((getGetTheaterMethod = TheaterServiceGrpc.getGetTheaterMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getGetTheaterMethod = TheaterServiceGrpc.getGetTheaterMethod) == null) {
          TheaterServiceGrpc.getGetTheaterMethod = getGetTheaterMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetTheaterRequest, com.microservices.movie.grpc.TheaterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTheater"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetTheaterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.TheaterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("GetTheater"))
              .build();
        }
      }
    }
    return getGetTheaterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListTheatersRequest,
      com.microservices.movie.grpc.ListTheatersResponse> getListTheatersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListTheaters",
      requestType = com.microservices.movie.grpc.ListTheatersRequest.class,
      responseType = com.microservices.movie.grpc.ListTheatersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListTheatersRequest,
      com.microservices.movie.grpc.ListTheatersResponse> getListTheatersMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListTheatersRequest, com.microservices.movie.grpc.ListTheatersResponse> getListTheatersMethod;
    if ((getListTheatersMethod = TheaterServiceGrpc.getListTheatersMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getListTheatersMethod = TheaterServiceGrpc.getListTheatersMethod) == null) {
          TheaterServiceGrpc.getListTheatersMethod = getListTheatersMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListTheatersRequest, com.microservices.movie.grpc.ListTheatersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListTheaters"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListTheatersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListTheatersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("ListTheaters"))
              .build();
        }
      }
    }
    return getListTheatersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.AddScreenRequest,
      com.microservices.movie.grpc.ScreenResponse> getAddScreenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddScreen",
      requestType = com.microservices.movie.grpc.AddScreenRequest.class,
      responseType = com.microservices.movie.grpc.ScreenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.AddScreenRequest,
      com.microservices.movie.grpc.ScreenResponse> getAddScreenMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.AddScreenRequest, com.microservices.movie.grpc.ScreenResponse> getAddScreenMethod;
    if ((getAddScreenMethod = TheaterServiceGrpc.getAddScreenMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getAddScreenMethod = TheaterServiceGrpc.getAddScreenMethod) == null) {
          TheaterServiceGrpc.getAddScreenMethod = getAddScreenMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.AddScreenRequest, com.microservices.movie.grpc.ScreenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddScreen"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.AddScreenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ScreenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("AddScreen"))
              .build();
        }
      }
    }
    return getAddScreenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateScreenRequest,
      com.microservices.movie.grpc.ScreenResponse> getUpdateScreenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateScreen",
      requestType = com.microservices.movie.grpc.UpdateScreenRequest.class,
      responseType = com.microservices.movie.grpc.ScreenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateScreenRequest,
      com.microservices.movie.grpc.ScreenResponse> getUpdateScreenMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateScreenRequest, com.microservices.movie.grpc.ScreenResponse> getUpdateScreenMethod;
    if ((getUpdateScreenMethod = TheaterServiceGrpc.getUpdateScreenMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getUpdateScreenMethod = TheaterServiceGrpc.getUpdateScreenMethod) == null) {
          TheaterServiceGrpc.getUpdateScreenMethod = getUpdateScreenMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.UpdateScreenRequest, com.microservices.movie.grpc.ScreenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateScreen"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UpdateScreenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ScreenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("UpdateScreen"))
              .build();
        }
      }
    }
    return getUpdateScreenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetScreenRequest,
      com.microservices.movie.grpc.ScreenResponse> getGetScreenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetScreen",
      requestType = com.microservices.movie.grpc.GetScreenRequest.class,
      responseType = com.microservices.movie.grpc.ScreenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetScreenRequest,
      com.microservices.movie.grpc.ScreenResponse> getGetScreenMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetScreenRequest, com.microservices.movie.grpc.ScreenResponse> getGetScreenMethod;
    if ((getGetScreenMethod = TheaterServiceGrpc.getGetScreenMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getGetScreenMethod = TheaterServiceGrpc.getGetScreenMethod) == null) {
          TheaterServiceGrpc.getGetScreenMethod = getGetScreenMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetScreenRequest, com.microservices.movie.grpc.ScreenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetScreen"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetScreenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ScreenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("GetScreen"))
              .build();
        }
      }
    }
    return getGetScreenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListScreensByTheaterRequest,
      com.microservices.movie.grpc.ListScreensResponse> getListScreensByTheaterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListScreensByTheater",
      requestType = com.microservices.movie.grpc.ListScreensByTheaterRequest.class,
      responseType = com.microservices.movie.grpc.ListScreensResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListScreensByTheaterRequest,
      com.microservices.movie.grpc.ListScreensResponse> getListScreensByTheaterMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListScreensByTheaterRequest, com.microservices.movie.grpc.ListScreensResponse> getListScreensByTheaterMethod;
    if ((getListScreensByTheaterMethod = TheaterServiceGrpc.getListScreensByTheaterMethod) == null) {
      synchronized (TheaterServiceGrpc.class) {
        if ((getListScreensByTheaterMethod = TheaterServiceGrpc.getListScreensByTheaterMethod) == null) {
          TheaterServiceGrpc.getListScreensByTheaterMethod = getListScreensByTheaterMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListScreensByTheaterRequest, com.microservices.movie.grpc.ListScreensResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListScreensByTheater"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListScreensByTheaterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListScreensResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TheaterServiceMethodDescriptorSupplier("ListScreensByTheater"))
              .build();
        }
      }
    }
    return getListScreensByTheaterMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TheaterServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TheaterServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TheaterServiceStub>() {
        @java.lang.Override
        public TheaterServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TheaterServiceStub(channel, callOptions);
        }
      };
    return TheaterServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TheaterServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TheaterServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TheaterServiceBlockingStub>() {
        @java.lang.Override
        public TheaterServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TheaterServiceBlockingStub(channel, callOptions);
        }
      };
    return TheaterServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TheaterServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TheaterServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TheaterServiceFutureStub>() {
        @java.lang.Override
        public TheaterServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TheaterServiceFutureStub(channel, callOptions);
        }
      };
    return TheaterServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createTheater(com.microservices.movie.grpc.CreateTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateTheaterMethod(), responseObserver);
    }

    /**
     */
    default void updateTheater(com.microservices.movie.grpc.UpdateTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateTheaterMethod(), responseObserver);
    }

    /**
     */
    default void getTheater(com.microservices.movie.grpc.GetTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTheaterMethod(), responseObserver);
    }

    /**
     */
    default void listTheaters(com.microservices.movie.grpc.ListTheatersRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListTheatersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListTheatersMethod(), responseObserver);
    }

    /**
     */
    default void addScreen(com.microservices.movie.grpc.AddScreenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddScreenMethod(), responseObserver);
    }

    /**
     */
    default void updateScreen(com.microservices.movie.grpc.UpdateScreenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateScreenMethod(), responseObserver);
    }

    /**
     */
    default void getScreen(com.microservices.movie.grpc.GetScreenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetScreenMethod(), responseObserver);
    }

    /**
     */
    default void listScreensByTheater(com.microservices.movie.grpc.ListScreensByTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListScreensResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListScreensByTheaterMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TheaterService.
   */
  public static abstract class TheaterServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TheaterServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TheaterService.
   */
  public static final class TheaterServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TheaterServiceStub> {
    private TheaterServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TheaterServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TheaterServiceStub(channel, callOptions);
    }

    /**
     */
    public void createTheater(com.microservices.movie.grpc.CreateTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateTheaterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateTheater(com.microservices.movie.grpc.UpdateTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateTheaterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTheater(com.microservices.movie.grpc.GetTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTheaterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listTheaters(com.microservices.movie.grpc.ListTheatersRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListTheatersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListTheatersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addScreen(com.microservices.movie.grpc.AddScreenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddScreenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateScreen(com.microservices.movie.grpc.UpdateScreenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateScreenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScreen(com.microservices.movie.grpc.GetScreenRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetScreenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listScreensByTheater(com.microservices.movie.grpc.ListScreensByTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListScreensResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListScreensByTheaterMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TheaterService.
   */
  public static final class TheaterServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TheaterServiceBlockingStub> {
    private TheaterServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TheaterServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TheaterServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.TheaterResponse createTheater(com.microservices.movie.grpc.CreateTheaterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTheaterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.TheaterResponse updateTheater(com.microservices.movie.grpc.UpdateTheaterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateTheaterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.TheaterResponse getTheater(com.microservices.movie.grpc.GetTheaterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTheaterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListTheatersResponse listTheaters(com.microservices.movie.grpc.ListTheatersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListTheatersMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ScreenResponse addScreen(com.microservices.movie.grpc.AddScreenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddScreenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ScreenResponse updateScreen(com.microservices.movie.grpc.UpdateScreenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateScreenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ScreenResponse getScreen(com.microservices.movie.grpc.GetScreenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetScreenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListScreensResponse listScreensByTheater(com.microservices.movie.grpc.ListScreensByTheaterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListScreensByTheaterMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TheaterService.
   */
  public static final class TheaterServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TheaterServiceFutureStub> {
    private TheaterServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TheaterServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TheaterServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.TheaterResponse> createTheater(
        com.microservices.movie.grpc.CreateTheaterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateTheaterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.TheaterResponse> updateTheater(
        com.microservices.movie.grpc.UpdateTheaterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateTheaterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.TheaterResponse> getTheater(
        com.microservices.movie.grpc.GetTheaterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTheaterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListTheatersResponse> listTheaters(
        com.microservices.movie.grpc.ListTheatersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListTheatersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ScreenResponse> addScreen(
        com.microservices.movie.grpc.AddScreenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddScreenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ScreenResponse> updateScreen(
        com.microservices.movie.grpc.UpdateScreenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateScreenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ScreenResponse> getScreen(
        com.microservices.movie.grpc.GetScreenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetScreenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListScreensResponse> listScreensByTheater(
        com.microservices.movie.grpc.ListScreensByTheaterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListScreensByTheaterMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_THEATER = 0;
  private static final int METHODID_UPDATE_THEATER = 1;
  private static final int METHODID_GET_THEATER = 2;
  private static final int METHODID_LIST_THEATERS = 3;
  private static final int METHODID_ADD_SCREEN = 4;
  private static final int METHODID_UPDATE_SCREEN = 5;
  private static final int METHODID_GET_SCREEN = 6;
  private static final int METHODID_LIST_SCREENS_BY_THEATER = 7;

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
        case METHODID_CREATE_THEATER:
          serviceImpl.createTheater((com.microservices.movie.grpc.CreateTheaterRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse>) responseObserver);
          break;
        case METHODID_UPDATE_THEATER:
          serviceImpl.updateTheater((com.microservices.movie.grpc.UpdateTheaterRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse>) responseObserver);
          break;
        case METHODID_GET_THEATER:
          serviceImpl.getTheater((com.microservices.movie.grpc.GetTheaterRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.TheaterResponse>) responseObserver);
          break;
        case METHODID_LIST_THEATERS:
          serviceImpl.listTheaters((com.microservices.movie.grpc.ListTheatersRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListTheatersResponse>) responseObserver);
          break;
        case METHODID_ADD_SCREEN:
          serviceImpl.addScreen((com.microservices.movie.grpc.AddScreenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse>) responseObserver);
          break;
        case METHODID_UPDATE_SCREEN:
          serviceImpl.updateScreen((com.microservices.movie.grpc.UpdateScreenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse>) responseObserver);
          break;
        case METHODID_GET_SCREEN:
          serviceImpl.getScreen((com.microservices.movie.grpc.GetScreenRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ScreenResponse>) responseObserver);
          break;
        case METHODID_LIST_SCREENS_BY_THEATER:
          serviceImpl.listScreensByTheater((com.microservices.movie.grpc.ListScreensByTheaterRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListScreensResponse>) responseObserver);
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
          getCreateTheaterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.CreateTheaterRequest,
              com.microservices.movie.grpc.TheaterResponse>(
                service, METHODID_CREATE_THEATER)))
        .addMethod(
          getUpdateTheaterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.UpdateTheaterRequest,
              com.microservices.movie.grpc.TheaterResponse>(
                service, METHODID_UPDATE_THEATER)))
        .addMethod(
          getGetTheaterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetTheaterRequest,
              com.microservices.movie.grpc.TheaterResponse>(
                service, METHODID_GET_THEATER)))
        .addMethod(
          getListTheatersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListTheatersRequest,
              com.microservices.movie.grpc.ListTheatersResponse>(
                service, METHODID_LIST_THEATERS)))
        .addMethod(
          getAddScreenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.AddScreenRequest,
              com.microservices.movie.grpc.ScreenResponse>(
                service, METHODID_ADD_SCREEN)))
        .addMethod(
          getUpdateScreenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.UpdateScreenRequest,
              com.microservices.movie.grpc.ScreenResponse>(
                service, METHODID_UPDATE_SCREEN)))
        .addMethod(
          getGetScreenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetScreenRequest,
              com.microservices.movie.grpc.ScreenResponse>(
                service, METHODID_GET_SCREEN)))
        .addMethod(
          getListScreensByTheaterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListScreensByTheaterRequest,
              com.microservices.movie.grpc.ListScreensResponse>(
                service, METHODID_LIST_SCREENS_BY_THEATER)))
        .build();
  }

  private static abstract class TheaterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TheaterServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TheaterService");
    }
  }

  private static final class TheaterServiceFileDescriptorSupplier
      extends TheaterServiceBaseDescriptorSupplier {
    TheaterServiceFileDescriptorSupplier() {}
  }

  private static final class TheaterServiceMethodDescriptorSupplier
      extends TheaterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TheaterServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TheaterServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TheaterServiceFileDescriptorSupplier())
              .addMethod(getCreateTheaterMethod())
              .addMethod(getUpdateTheaterMethod())
              .addMethod(getGetTheaterMethod())
              .addMethod(getListTheatersMethod())
              .addMethod(getAddScreenMethod())
              .addMethod(getUpdateScreenMethod())
              .addMethod(getGetScreenMethod())
              .addMethod(getListScreensByTheaterMethod())
              .build();
        }
      }
    }
    return result;
  }
}
