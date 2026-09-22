package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ShowServiceGrpc {

  private ShowServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.ShowService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateShowRequest,
      com.microservices.movie.grpc.ShowResponse> getCreateShowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateShow",
      requestType = com.microservices.movie.grpc.CreateShowRequest.class,
      responseType = com.microservices.movie.grpc.ShowResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateShowRequest,
      com.microservices.movie.grpc.ShowResponse> getCreateShowMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateShowRequest, com.microservices.movie.grpc.ShowResponse> getCreateShowMethod;
    if ((getCreateShowMethod = ShowServiceGrpc.getCreateShowMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getCreateShowMethod = ShowServiceGrpc.getCreateShowMethod) == null) {
          ShowServiceGrpc.getCreateShowMethod = getCreateShowMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.CreateShowRequest, com.microservices.movie.grpc.ShowResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateShow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CreateShowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ShowResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("CreateShow"))
              .build();
        }
      }
    }
    return getCreateShowMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateShowRequest,
      com.microservices.movie.grpc.ShowResponse> getUpdateShowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateShow",
      requestType = com.microservices.movie.grpc.UpdateShowRequest.class,
      responseType = com.microservices.movie.grpc.ShowResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateShowRequest,
      com.microservices.movie.grpc.ShowResponse> getUpdateShowMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateShowRequest, com.microservices.movie.grpc.ShowResponse> getUpdateShowMethod;
    if ((getUpdateShowMethod = ShowServiceGrpc.getUpdateShowMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getUpdateShowMethod = ShowServiceGrpc.getUpdateShowMethod) == null) {
          ShowServiceGrpc.getUpdateShowMethod = getUpdateShowMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.UpdateShowRequest, com.microservices.movie.grpc.ShowResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateShow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UpdateShowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ShowResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("UpdateShow"))
              .build();
        }
      }
    }
    return getUpdateShowMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetShowRequest,
      com.microservices.movie.grpc.ShowResponse> getGetShowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetShow",
      requestType = com.microservices.movie.grpc.GetShowRequest.class,
      responseType = com.microservices.movie.grpc.ShowResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetShowRequest,
      com.microservices.movie.grpc.ShowResponse> getGetShowMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetShowRequest, com.microservices.movie.grpc.ShowResponse> getGetShowMethod;
    if ((getGetShowMethod = ShowServiceGrpc.getGetShowMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getGetShowMethod = ShowServiceGrpc.getGetShowMethod) == null) {
          ShowServiceGrpc.getGetShowMethod = getGetShowMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetShowRequest, com.microservices.movie.grpc.ShowResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetShow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetShowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ShowResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("GetShow"))
              .build();
        }
      }
    }
    return getGetShowMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsRequest,
      com.microservices.movie.grpc.ListShowsResponse> getListShowsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListShows",
      requestType = com.microservices.movie.grpc.ListShowsRequest.class,
      responseType = com.microservices.movie.grpc.ListShowsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsRequest,
      com.microservices.movie.grpc.ListShowsResponse> getListShowsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsRequest, com.microservices.movie.grpc.ListShowsResponse> getListShowsMethod;
    if ((getListShowsMethod = ShowServiceGrpc.getListShowsMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getListShowsMethod = ShowServiceGrpc.getListShowsMethod) == null) {
          ShowServiceGrpc.getListShowsMethod = getListShowsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListShowsRequest, com.microservices.movie.grpc.ListShowsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListShows"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("ListShows"))
              .build();
        }
      }
    }
    return getListShowsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsByMovieRequest,
      com.microservices.movie.grpc.ListShowsResponse> getListShowsByMovieMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListShowsByMovie",
      requestType = com.microservices.movie.grpc.ListShowsByMovieRequest.class,
      responseType = com.microservices.movie.grpc.ListShowsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsByMovieRequest,
      com.microservices.movie.grpc.ListShowsResponse> getListShowsByMovieMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsByMovieRequest, com.microservices.movie.grpc.ListShowsResponse> getListShowsByMovieMethod;
    if ((getListShowsByMovieMethod = ShowServiceGrpc.getListShowsByMovieMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getListShowsByMovieMethod = ShowServiceGrpc.getListShowsByMovieMethod) == null) {
          ShowServiceGrpc.getListShowsByMovieMethod = getListShowsByMovieMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListShowsByMovieRequest, com.microservices.movie.grpc.ListShowsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListShowsByMovie"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsByMovieRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("ListShowsByMovie"))
              .build();
        }
      }
    }
    return getListShowsByMovieMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsByTheaterRequest,
      com.microservices.movie.grpc.ListShowsResponse> getListShowsByTheaterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListShowsByTheater",
      requestType = com.microservices.movie.grpc.ListShowsByTheaterRequest.class,
      responseType = com.microservices.movie.grpc.ListShowsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsByTheaterRequest,
      com.microservices.movie.grpc.ListShowsResponse> getListShowsByTheaterMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListShowsByTheaterRequest, com.microservices.movie.grpc.ListShowsResponse> getListShowsByTheaterMethod;
    if ((getListShowsByTheaterMethod = ShowServiceGrpc.getListShowsByTheaterMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getListShowsByTheaterMethod = ShowServiceGrpc.getListShowsByTheaterMethod) == null) {
          ShowServiceGrpc.getListShowsByTheaterMethod = getListShowsByTheaterMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListShowsByTheaterRequest, com.microservices.movie.grpc.ListShowsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListShowsByTheater"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsByTheaterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("ListShowsByTheater"))
              .build();
        }
      }
    }
    return getListShowsByTheaterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.SearchShowsRequest,
      com.microservices.movie.grpc.ListShowsResponse> getSearchShowsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchShows",
      requestType = com.microservices.movie.grpc.SearchShowsRequest.class,
      responseType = com.microservices.movie.grpc.ListShowsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.SearchShowsRequest,
      com.microservices.movie.grpc.ListShowsResponse> getSearchShowsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.SearchShowsRequest, com.microservices.movie.grpc.ListShowsResponse> getSearchShowsMethod;
    if ((getSearchShowsMethod = ShowServiceGrpc.getSearchShowsMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getSearchShowsMethod = ShowServiceGrpc.getSearchShowsMethod) == null) {
          ShowServiceGrpc.getSearchShowsMethod = getSearchShowsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.SearchShowsRequest, com.microservices.movie.grpc.ListShowsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchShows"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.SearchShowsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListShowsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("SearchShows"))
              .build();
        }
      }
    }
    return getSearchShowsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetAvailableSeatsRequest,
      com.microservices.movie.grpc.AvailableSeatsResponse> getGetAvailableSeatsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAvailableSeats",
      requestType = com.microservices.movie.grpc.GetAvailableSeatsRequest.class,
      responseType = com.microservices.movie.grpc.AvailableSeatsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetAvailableSeatsRequest,
      com.microservices.movie.grpc.AvailableSeatsResponse> getGetAvailableSeatsMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetAvailableSeatsRequest, com.microservices.movie.grpc.AvailableSeatsResponse> getGetAvailableSeatsMethod;
    if ((getGetAvailableSeatsMethod = ShowServiceGrpc.getGetAvailableSeatsMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getGetAvailableSeatsMethod = ShowServiceGrpc.getGetAvailableSeatsMethod) == null) {
          ShowServiceGrpc.getGetAvailableSeatsMethod = getGetAvailableSeatsMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetAvailableSeatsRequest, com.microservices.movie.grpc.AvailableSeatsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAvailableSeats"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetAvailableSeatsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.AvailableSeatsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("GetAvailableSeats"))
              .build();
        }
      }
    }
    return getGetAvailableSeatsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetAvailableShowTimesRequest,
      com.microservices.movie.grpc.AvailableShowTimesResponse> getGetAvailableShowTimesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAvailableShowTimes",
      requestType = com.microservices.movie.grpc.GetAvailableShowTimesRequest.class,
      responseType = com.microservices.movie.grpc.AvailableShowTimesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetAvailableShowTimesRequest,
      com.microservices.movie.grpc.AvailableShowTimesResponse> getGetAvailableShowTimesMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetAvailableShowTimesRequest, com.microservices.movie.grpc.AvailableShowTimesResponse> getGetAvailableShowTimesMethod;
    if ((getGetAvailableShowTimesMethod = ShowServiceGrpc.getGetAvailableShowTimesMethod) == null) {
      synchronized (ShowServiceGrpc.class) {
        if ((getGetAvailableShowTimesMethod = ShowServiceGrpc.getGetAvailableShowTimesMethod) == null) {
          ShowServiceGrpc.getGetAvailableShowTimesMethod = getGetAvailableShowTimesMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetAvailableShowTimesRequest, com.microservices.movie.grpc.AvailableShowTimesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAvailableShowTimes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetAvailableShowTimesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.AvailableShowTimesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShowServiceMethodDescriptorSupplier("GetAvailableShowTimes"))
              .build();
        }
      }
    }
    return getGetAvailableShowTimesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ShowServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ShowServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ShowServiceStub>() {
        @java.lang.Override
        public ShowServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ShowServiceStub(channel, callOptions);
        }
      };
    return ShowServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ShowServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ShowServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ShowServiceBlockingStub>() {
        @java.lang.Override
        public ShowServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ShowServiceBlockingStub(channel, callOptions);
        }
      };
    return ShowServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ShowServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ShowServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ShowServiceFutureStub>() {
        @java.lang.Override
        public ShowServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ShowServiceFutureStub(channel, callOptions);
        }
      };
    return ShowServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createShow(com.microservices.movie.grpc.CreateShowRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateShowMethod(), responseObserver);
    }

    /**
     */
    default void updateShow(com.microservices.movie.grpc.UpdateShowRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateShowMethod(), responseObserver);
    }

    /**
     */
    default void getShow(com.microservices.movie.grpc.GetShowRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetShowMethod(), responseObserver);
    }

    /**
     */
    default void listShows(com.microservices.movie.grpc.ListShowsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListShowsMethod(), responseObserver);
    }

    /**
     */
    default void listShowsByMovie(com.microservices.movie.grpc.ListShowsByMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListShowsByMovieMethod(), responseObserver);
    }

    /**
     */
    default void listShowsByTheater(com.microservices.movie.grpc.ListShowsByTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListShowsByTheaterMethod(), responseObserver);
    }

    /**
     */
    default void searchShows(com.microservices.movie.grpc.SearchShowsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchShowsMethod(), responseObserver);
    }

    /**
     */
    default void getAvailableSeats(com.microservices.movie.grpc.GetAvailableSeatsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.AvailableSeatsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAvailableSeatsMethod(), responseObserver);
    }

    /**
     */
    default void getAvailableShowTimes(com.microservices.movie.grpc.GetAvailableShowTimesRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.AvailableShowTimesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAvailableShowTimesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ShowService.
   */
  public static abstract class ShowServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ShowServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ShowService.
   */
  public static final class ShowServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ShowServiceStub> {
    private ShowServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ShowServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ShowServiceStub(channel, callOptions);
    }

    /**
     */
    public void createShow(com.microservices.movie.grpc.CreateShowRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateShowMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateShow(com.microservices.movie.grpc.UpdateShowRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateShowMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getShow(com.microservices.movie.grpc.GetShowRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetShowMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listShows(com.microservices.movie.grpc.ListShowsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListShowsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listShowsByMovie(com.microservices.movie.grpc.ListShowsByMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListShowsByMovieMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listShowsByTheater(com.microservices.movie.grpc.ListShowsByTheaterRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListShowsByTheaterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchShows(com.microservices.movie.grpc.SearchShowsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchShowsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAvailableSeats(com.microservices.movie.grpc.GetAvailableSeatsRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.AvailableSeatsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAvailableSeatsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAvailableShowTimes(com.microservices.movie.grpc.GetAvailableShowTimesRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.AvailableShowTimesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAvailableShowTimesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ShowService.
   */
  public static final class ShowServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ShowServiceBlockingStub> {
    private ShowServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ShowServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ShowServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.ShowResponse createShow(com.microservices.movie.grpc.CreateShowRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateShowMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ShowResponse updateShow(com.microservices.movie.grpc.UpdateShowRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateShowMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ShowResponse getShow(com.microservices.movie.grpc.GetShowRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetShowMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListShowsResponse listShows(com.microservices.movie.grpc.ListShowsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListShowsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListShowsResponse listShowsByMovie(com.microservices.movie.grpc.ListShowsByMovieRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListShowsByMovieMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListShowsResponse listShowsByTheater(com.microservices.movie.grpc.ListShowsByTheaterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListShowsByTheaterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListShowsResponse searchShows(com.microservices.movie.grpc.SearchShowsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchShowsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.AvailableSeatsResponse getAvailableSeats(com.microservices.movie.grpc.GetAvailableSeatsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAvailableSeatsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.AvailableShowTimesResponse getAvailableShowTimes(com.microservices.movie.grpc.GetAvailableShowTimesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAvailableShowTimesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ShowService.
   */
  public static final class ShowServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ShowServiceFutureStub> {
    private ShowServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ShowServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ShowServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ShowResponse> createShow(
        com.microservices.movie.grpc.CreateShowRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateShowMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ShowResponse> updateShow(
        com.microservices.movie.grpc.UpdateShowRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateShowMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ShowResponse> getShow(
        com.microservices.movie.grpc.GetShowRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetShowMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListShowsResponse> listShows(
        com.microservices.movie.grpc.ListShowsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListShowsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListShowsResponse> listShowsByMovie(
        com.microservices.movie.grpc.ListShowsByMovieRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListShowsByMovieMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListShowsResponse> listShowsByTheater(
        com.microservices.movie.grpc.ListShowsByTheaterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListShowsByTheaterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListShowsResponse> searchShows(
        com.microservices.movie.grpc.SearchShowsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchShowsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.AvailableSeatsResponse> getAvailableSeats(
        com.microservices.movie.grpc.GetAvailableSeatsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAvailableSeatsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.AvailableShowTimesResponse> getAvailableShowTimes(
        com.microservices.movie.grpc.GetAvailableShowTimesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAvailableShowTimesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_SHOW = 0;
  private static final int METHODID_UPDATE_SHOW = 1;
  private static final int METHODID_GET_SHOW = 2;
  private static final int METHODID_LIST_SHOWS = 3;
  private static final int METHODID_LIST_SHOWS_BY_MOVIE = 4;
  private static final int METHODID_LIST_SHOWS_BY_THEATER = 5;
  private static final int METHODID_SEARCH_SHOWS = 6;
  private static final int METHODID_GET_AVAILABLE_SEATS = 7;
  private static final int METHODID_GET_AVAILABLE_SHOW_TIMES = 8;

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
        case METHODID_CREATE_SHOW:
          serviceImpl.createShow((com.microservices.movie.grpc.CreateShowRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse>) responseObserver);
          break;
        case METHODID_UPDATE_SHOW:
          serviceImpl.updateShow((com.microservices.movie.grpc.UpdateShowRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse>) responseObserver);
          break;
        case METHODID_GET_SHOW:
          serviceImpl.getShow((com.microservices.movie.grpc.GetShowRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ShowResponse>) responseObserver);
          break;
        case METHODID_LIST_SHOWS:
          serviceImpl.listShows((com.microservices.movie.grpc.ListShowsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse>) responseObserver);
          break;
        case METHODID_LIST_SHOWS_BY_MOVIE:
          serviceImpl.listShowsByMovie((com.microservices.movie.grpc.ListShowsByMovieRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse>) responseObserver);
          break;
        case METHODID_LIST_SHOWS_BY_THEATER:
          serviceImpl.listShowsByTheater((com.microservices.movie.grpc.ListShowsByTheaterRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse>) responseObserver);
          break;
        case METHODID_SEARCH_SHOWS:
          serviceImpl.searchShows((com.microservices.movie.grpc.SearchShowsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListShowsResponse>) responseObserver);
          break;
        case METHODID_GET_AVAILABLE_SEATS:
          serviceImpl.getAvailableSeats((com.microservices.movie.grpc.GetAvailableSeatsRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.AvailableSeatsResponse>) responseObserver);
          break;
        case METHODID_GET_AVAILABLE_SHOW_TIMES:
          serviceImpl.getAvailableShowTimes((com.microservices.movie.grpc.GetAvailableShowTimesRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.AvailableShowTimesResponse>) responseObserver);
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
          getCreateShowMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.CreateShowRequest,
              com.microservices.movie.grpc.ShowResponse>(
                service, METHODID_CREATE_SHOW)))
        .addMethod(
          getUpdateShowMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.UpdateShowRequest,
              com.microservices.movie.grpc.ShowResponse>(
                service, METHODID_UPDATE_SHOW)))
        .addMethod(
          getGetShowMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetShowRequest,
              com.microservices.movie.grpc.ShowResponse>(
                service, METHODID_GET_SHOW)))
        .addMethod(
          getListShowsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListShowsRequest,
              com.microservices.movie.grpc.ListShowsResponse>(
                service, METHODID_LIST_SHOWS)))
        .addMethod(
          getListShowsByMovieMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListShowsByMovieRequest,
              com.microservices.movie.grpc.ListShowsResponse>(
                service, METHODID_LIST_SHOWS_BY_MOVIE)))
        .addMethod(
          getListShowsByTheaterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListShowsByTheaterRequest,
              com.microservices.movie.grpc.ListShowsResponse>(
                service, METHODID_LIST_SHOWS_BY_THEATER)))
        .addMethod(
          getSearchShowsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.SearchShowsRequest,
              com.microservices.movie.grpc.ListShowsResponse>(
                service, METHODID_SEARCH_SHOWS)))
        .addMethod(
          getGetAvailableSeatsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetAvailableSeatsRequest,
              com.microservices.movie.grpc.AvailableSeatsResponse>(
                service, METHODID_GET_AVAILABLE_SEATS)))
        .addMethod(
          getGetAvailableShowTimesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetAvailableShowTimesRequest,
              com.microservices.movie.grpc.AvailableShowTimesResponse>(
                service, METHODID_GET_AVAILABLE_SHOW_TIMES)))
        .build();
  }

  private static abstract class ShowServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ShowServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ShowService");
    }
  }

  private static final class ShowServiceFileDescriptorSupplier
      extends ShowServiceBaseDescriptorSupplier {
    ShowServiceFileDescriptorSupplier() {}
  }

  private static final class ShowServiceMethodDescriptorSupplier
      extends ShowServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ShowServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ShowServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ShowServiceFileDescriptorSupplier())
              .addMethod(getCreateShowMethod())
              .addMethod(getUpdateShowMethod())
              .addMethod(getGetShowMethod())
              .addMethod(getListShowsMethod())
              .addMethod(getListShowsByMovieMethod())
              .addMethod(getListShowsByTheaterMethod())
              .addMethod(getSearchShowsMethod())
              .addMethod(getGetAvailableSeatsMethod())
              .addMethod(getGetAvailableShowTimesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
