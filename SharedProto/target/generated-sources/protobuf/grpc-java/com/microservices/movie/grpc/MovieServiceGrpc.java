package com.microservices.movie.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: Movie.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MovieServiceGrpc {

  private MovieServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "movie.MovieService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateMovieRequest,
      com.microservices.movie.grpc.MovieResponse> getCreateMovieMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateMovie",
      requestType = com.microservices.movie.grpc.CreateMovieRequest.class,
      responseType = com.microservices.movie.grpc.MovieResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateMovieRequest,
      com.microservices.movie.grpc.MovieResponse> getCreateMovieMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.CreateMovieRequest, com.microservices.movie.grpc.MovieResponse> getCreateMovieMethod;
    if ((getCreateMovieMethod = MovieServiceGrpc.getCreateMovieMethod) == null) {
      synchronized (MovieServiceGrpc.class) {
        if ((getCreateMovieMethod = MovieServiceGrpc.getCreateMovieMethod) == null) {
          MovieServiceGrpc.getCreateMovieMethod = getCreateMovieMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.CreateMovieRequest, com.microservices.movie.grpc.MovieResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateMovie"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.CreateMovieRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.MovieResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MovieServiceMethodDescriptorSupplier("CreateMovie"))
              .build();
        }
      }
    }
    return getCreateMovieMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateMovieRequest,
      com.microservices.movie.grpc.MovieResponse> getUpdateMovieMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateMovie",
      requestType = com.microservices.movie.grpc.UpdateMovieRequest.class,
      responseType = com.microservices.movie.grpc.MovieResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateMovieRequest,
      com.microservices.movie.grpc.MovieResponse> getUpdateMovieMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.UpdateMovieRequest, com.microservices.movie.grpc.MovieResponse> getUpdateMovieMethod;
    if ((getUpdateMovieMethod = MovieServiceGrpc.getUpdateMovieMethod) == null) {
      synchronized (MovieServiceGrpc.class) {
        if ((getUpdateMovieMethod = MovieServiceGrpc.getUpdateMovieMethod) == null) {
          MovieServiceGrpc.getUpdateMovieMethod = getUpdateMovieMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.UpdateMovieRequest, com.microservices.movie.grpc.MovieResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateMovie"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.UpdateMovieRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.MovieResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MovieServiceMethodDescriptorSupplier("UpdateMovie"))
              .build();
        }
      }
    }
    return getUpdateMovieMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetMovieRequest,
      com.microservices.movie.grpc.MovieResponse> getGetMovieMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMovie",
      requestType = com.microservices.movie.grpc.GetMovieRequest.class,
      responseType = com.microservices.movie.grpc.MovieResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetMovieRequest,
      com.microservices.movie.grpc.MovieResponse> getGetMovieMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.GetMovieRequest, com.microservices.movie.grpc.MovieResponse> getGetMovieMethod;
    if ((getGetMovieMethod = MovieServiceGrpc.getGetMovieMethod) == null) {
      synchronized (MovieServiceGrpc.class) {
        if ((getGetMovieMethod = MovieServiceGrpc.getGetMovieMethod) == null) {
          MovieServiceGrpc.getGetMovieMethod = getGetMovieMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.GetMovieRequest, com.microservices.movie.grpc.MovieResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMovie"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.GetMovieRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.MovieResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MovieServiceMethodDescriptorSupplier("GetMovie"))
              .build();
        }
      }
    }
    return getGetMovieMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListMoviesRequest,
      com.microservices.movie.grpc.ListMoviesResponse> getListMoviesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListMovies",
      requestType = com.microservices.movie.grpc.ListMoviesRequest.class,
      responseType = com.microservices.movie.grpc.ListMoviesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListMoviesRequest,
      com.microservices.movie.grpc.ListMoviesResponse> getListMoviesMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.ListMoviesRequest, com.microservices.movie.grpc.ListMoviesResponse> getListMoviesMethod;
    if ((getListMoviesMethod = MovieServiceGrpc.getListMoviesMethod) == null) {
      synchronized (MovieServiceGrpc.class) {
        if ((getListMoviesMethod = MovieServiceGrpc.getListMoviesMethod) == null) {
          MovieServiceGrpc.getListMoviesMethod = getListMoviesMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.ListMoviesRequest, com.microservices.movie.grpc.ListMoviesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListMovies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListMoviesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListMoviesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MovieServiceMethodDescriptorSupplier("ListMovies"))
              .build();
        }
      }
    }
    return getListMoviesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.SearchMoviesRequest,
      com.microservices.movie.grpc.ListMoviesResponse> getSearchMoviesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchMovies",
      requestType = com.microservices.movie.grpc.SearchMoviesRequest.class,
      responseType = com.microservices.movie.grpc.ListMoviesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.SearchMoviesRequest,
      com.microservices.movie.grpc.ListMoviesResponse> getSearchMoviesMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.SearchMoviesRequest, com.microservices.movie.grpc.ListMoviesResponse> getSearchMoviesMethod;
    if ((getSearchMoviesMethod = MovieServiceGrpc.getSearchMoviesMethod) == null) {
      synchronized (MovieServiceGrpc.class) {
        if ((getSearchMoviesMethod = MovieServiceGrpc.getSearchMoviesMethod) == null) {
          MovieServiceGrpc.getSearchMoviesMethod = getSearchMoviesMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.SearchMoviesRequest, com.microservices.movie.grpc.ListMoviesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchMovies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.SearchMoviesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.ListMoviesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MovieServiceMethodDescriptorSupplier("SearchMovies"))
              .build();
        }
      }
    }
    return getSearchMoviesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.microservices.movie.grpc.DeleteMovieRequest,
      com.microservices.movie.grpc.DeleteMovieResponse> getDeleteMovieMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteMovie",
      requestType = com.microservices.movie.grpc.DeleteMovieRequest.class,
      responseType = com.microservices.movie.grpc.DeleteMovieResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.microservices.movie.grpc.DeleteMovieRequest,
      com.microservices.movie.grpc.DeleteMovieResponse> getDeleteMovieMethod() {
    io.grpc.MethodDescriptor<com.microservices.movie.grpc.DeleteMovieRequest, com.microservices.movie.grpc.DeleteMovieResponse> getDeleteMovieMethod;
    if ((getDeleteMovieMethod = MovieServiceGrpc.getDeleteMovieMethod) == null) {
      synchronized (MovieServiceGrpc.class) {
        if ((getDeleteMovieMethod = MovieServiceGrpc.getDeleteMovieMethod) == null) {
          MovieServiceGrpc.getDeleteMovieMethod = getDeleteMovieMethod =
              io.grpc.MethodDescriptor.<com.microservices.movie.grpc.DeleteMovieRequest, com.microservices.movie.grpc.DeleteMovieResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteMovie"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.DeleteMovieRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.microservices.movie.grpc.DeleteMovieResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MovieServiceMethodDescriptorSupplier("DeleteMovie"))
              .build();
        }
      }
    }
    return getDeleteMovieMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MovieServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MovieServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MovieServiceStub>() {
        @java.lang.Override
        public MovieServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MovieServiceStub(channel, callOptions);
        }
      };
    return MovieServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MovieServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MovieServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MovieServiceBlockingStub>() {
        @java.lang.Override
        public MovieServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MovieServiceBlockingStub(channel, callOptions);
        }
      };
    return MovieServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MovieServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MovieServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MovieServiceFutureStub>() {
        @java.lang.Override
        public MovieServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MovieServiceFutureStub(channel, callOptions);
        }
      };
    return MovieServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createMovie(com.microservices.movie.grpc.CreateMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateMovieMethod(), responseObserver);
    }

    /**
     */
    default void updateMovie(com.microservices.movie.grpc.UpdateMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateMovieMethod(), responseObserver);
    }

    /**
     */
    default void getMovie(com.microservices.movie.grpc.GetMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMovieMethod(), responseObserver);
    }

    /**
     */
    default void listMovies(com.microservices.movie.grpc.ListMoviesRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListMoviesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListMoviesMethod(), responseObserver);
    }

    /**
     */
    default void searchMovies(com.microservices.movie.grpc.SearchMoviesRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListMoviesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchMoviesMethod(), responseObserver);
    }

    /**
     */
    default void deleteMovie(com.microservices.movie.grpc.DeleteMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.DeleteMovieResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMovieMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MovieService.
   */
  public static abstract class MovieServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MovieServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MovieService.
   */
  public static final class MovieServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MovieServiceStub> {
    private MovieServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MovieServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MovieServiceStub(channel, callOptions);
    }

    /**
     */
    public void createMovie(com.microservices.movie.grpc.CreateMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateMovieMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateMovie(com.microservices.movie.grpc.UpdateMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateMovieMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMovie(com.microservices.movie.grpc.GetMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMovieMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listMovies(com.microservices.movie.grpc.ListMoviesRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListMoviesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListMoviesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchMovies(com.microservices.movie.grpc.SearchMoviesRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListMoviesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchMoviesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteMovie(com.microservices.movie.grpc.DeleteMovieRequest request,
        io.grpc.stub.StreamObserver<com.microservices.movie.grpc.DeleteMovieResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMovieMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MovieService.
   */
  public static final class MovieServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MovieServiceBlockingStub> {
    private MovieServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MovieServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MovieServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.microservices.movie.grpc.MovieResponse createMovie(com.microservices.movie.grpc.CreateMovieRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateMovieMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.MovieResponse updateMovie(com.microservices.movie.grpc.UpdateMovieRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMovieMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.MovieResponse getMovie(com.microservices.movie.grpc.GetMovieRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMovieMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListMoviesResponse listMovies(com.microservices.movie.grpc.ListMoviesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListMoviesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.ListMoviesResponse searchMovies(com.microservices.movie.grpc.SearchMoviesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchMoviesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.microservices.movie.grpc.DeleteMovieResponse deleteMovie(com.microservices.movie.grpc.DeleteMovieRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMovieMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MovieService.
   */
  public static final class MovieServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MovieServiceFutureStub> {
    private MovieServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MovieServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MovieServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.MovieResponse> createMovie(
        com.microservices.movie.grpc.CreateMovieRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateMovieMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.MovieResponse> updateMovie(
        com.microservices.movie.grpc.UpdateMovieRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMovieMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.MovieResponse> getMovie(
        com.microservices.movie.grpc.GetMovieRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMovieMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListMoviesResponse> listMovies(
        com.microservices.movie.grpc.ListMoviesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListMoviesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.ListMoviesResponse> searchMovies(
        com.microservices.movie.grpc.SearchMoviesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchMoviesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.microservices.movie.grpc.DeleteMovieResponse> deleteMovie(
        com.microservices.movie.grpc.DeleteMovieRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMovieMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_MOVIE = 0;
  private static final int METHODID_UPDATE_MOVIE = 1;
  private static final int METHODID_GET_MOVIE = 2;
  private static final int METHODID_LIST_MOVIES = 3;
  private static final int METHODID_SEARCH_MOVIES = 4;
  private static final int METHODID_DELETE_MOVIE = 5;

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
        case METHODID_CREATE_MOVIE:
          serviceImpl.createMovie((com.microservices.movie.grpc.CreateMovieRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse>) responseObserver);
          break;
        case METHODID_UPDATE_MOVIE:
          serviceImpl.updateMovie((com.microservices.movie.grpc.UpdateMovieRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse>) responseObserver);
          break;
        case METHODID_GET_MOVIE:
          serviceImpl.getMovie((com.microservices.movie.grpc.GetMovieRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.MovieResponse>) responseObserver);
          break;
        case METHODID_LIST_MOVIES:
          serviceImpl.listMovies((com.microservices.movie.grpc.ListMoviesRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListMoviesResponse>) responseObserver);
          break;
        case METHODID_SEARCH_MOVIES:
          serviceImpl.searchMovies((com.microservices.movie.grpc.SearchMoviesRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.ListMoviesResponse>) responseObserver);
          break;
        case METHODID_DELETE_MOVIE:
          serviceImpl.deleteMovie((com.microservices.movie.grpc.DeleteMovieRequest) request,
              (io.grpc.stub.StreamObserver<com.microservices.movie.grpc.DeleteMovieResponse>) responseObserver);
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
          getCreateMovieMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.CreateMovieRequest,
              com.microservices.movie.grpc.MovieResponse>(
                service, METHODID_CREATE_MOVIE)))
        .addMethod(
          getUpdateMovieMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.UpdateMovieRequest,
              com.microservices.movie.grpc.MovieResponse>(
                service, METHODID_UPDATE_MOVIE)))
        .addMethod(
          getGetMovieMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.GetMovieRequest,
              com.microservices.movie.grpc.MovieResponse>(
                service, METHODID_GET_MOVIE)))
        .addMethod(
          getListMoviesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.ListMoviesRequest,
              com.microservices.movie.grpc.ListMoviesResponse>(
                service, METHODID_LIST_MOVIES)))
        .addMethod(
          getSearchMoviesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.SearchMoviesRequest,
              com.microservices.movie.grpc.ListMoviesResponse>(
                service, METHODID_SEARCH_MOVIES)))
        .addMethod(
          getDeleteMovieMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.microservices.movie.grpc.DeleteMovieRequest,
              com.microservices.movie.grpc.DeleteMovieResponse>(
                service, METHODID_DELETE_MOVIE)))
        .build();
  }

  private static abstract class MovieServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MovieServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.microservices.movie.grpc.Movie.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MovieService");
    }
  }

  private static final class MovieServiceFileDescriptorSupplier
      extends MovieServiceBaseDescriptorSupplier {
    MovieServiceFileDescriptorSupplier() {}
  }

  private static final class MovieServiceMethodDescriptorSupplier
      extends MovieServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MovieServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MovieServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MovieServiceFileDescriptorSupplier())
              .addMethod(getCreateMovieMethod())
              .addMethod(getUpdateMovieMethod())
              .addMethod(getGetMovieMethod())
              .addMethod(getListMoviesMethod())
              .addMethod(getSearchMoviesMethod())
              .addMethod(getDeleteMovieMethod())
              .build();
        }
      }
    }
    return result;
  }
}
