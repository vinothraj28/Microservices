package com.microservices.movie.services.grpcServices;


import com.microservices.movie.grpc.*;
import com.microservices.movie.mappers.ScreenMapper;
import com.microservices.movie.mappers.TheaterMapper;
import com.microservices.movie.models.entities.Screen;
import com.microservices.movie.models.entities.Theater;
import com.microservices.movie.services.interfaces.TheaterService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Slf4j
@GrpcService
public class TheaterGrpcService extends TheaterServiceGrpc.TheaterServiceImplBase {

    private final TheaterService theaterService;
    private final TheaterMapper theaterMapper;
    private final ScreenMapper screenMapper;

    public TheaterGrpcService(TheaterService theaterService, TheaterMapper theaterMapper, ScreenMapper screenMapper) {
        this.theaterService = theaterService;
        this.theaterMapper = theaterMapper;
        this.screenMapper = screenMapper;
    }


    @Override
    public void createTheater(CreateTheaterRequest createTheaterRequest,
                              StreamObserver<TheaterResponse> responseStreamObserver) {
        try{
            Theater theater = theaterMapper.toTheater(createTheaterRequest);
            Theater savedTheater = theaterService.createTheater(theater);
            TheaterResponse theaterResponse = theaterMapper.toTheaterResponse(savedTheater);
            responseStreamObserver.onNext(theaterResponse);
            responseStreamObserver.onCompleted();
        }catch (Exception e) {
            log.error("Error creating theater", e);
            responseStreamObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void listTheaters(ListTheatersRequest listTheatersRequest,
                            StreamObserver<ListTheatersResponse> responseStreamObserver){

        try{
            // Implement the logic to list theaters based on the request parameters
            Page<Theater> theaters = theaterService.listTheaters(listTheatersRequest.getPage(), listTheatersRequest.getSize(), listTheatersRequest.getCity());

            ListTheatersResponse.Builder responseBuilder = ListTheatersResponse.newBuilder();
            theaters.forEach(theater -> responseBuilder.addTheaters(theaterMapper.toTheaterResponse(theater)));
            responseBuilder.setPage(listTheatersRequest.getPage());
            responseBuilder.setSize(listTheatersRequest.getSize());
            responseBuilder.setTotalCount((int) theaters.getTotalElements());
            responseBuilder.setTotalPages(theaters.getTotalPages());
            responseStreamObserver.onNext(responseBuilder.build());
            responseStreamObserver.onCompleted();

        }catch (Exception e) {
            log.error("Error listing theaters", e);
            responseStreamObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }

    }

    @Override
    public void getTheater(GetTheaterRequest getTheaterByIdRequest,
                            StreamObserver<TheaterResponse> responseStreamObserver) {
        try{
            Theater theater = theaterService.getTheater(UUID.fromString(getTheaterByIdRequest.getTheaterId()));
            TheaterResponse theaterResponse = theaterMapper.toTheaterResponse(theater);
            responseStreamObserver.onNext(theaterResponse);
            responseStreamObserver.onCompleted();
        }catch (IllegalArgumentException e) {
            responseStreamObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid theater ID")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error retrieving theater", e);
            responseStreamObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }

    }

    @Override
    public void updateTheater(UpdateTheaterRequest updateTheaterRequest,
                              StreamObserver<TheaterResponse> responseStreamObserver) {

        try{
            Theater theater = theaterMapper.toTheater(updateTheaterRequest);

            Theater updatedTheater = theaterService.updateTheater(theater.getId(), theater);

            TheaterResponse theaterResponse = theaterMapper.toTheaterResponse(updatedTheater);
            responseStreamObserver.onNext(theaterResponse);
            responseStreamObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseStreamObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid theater ID")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error updating theater", e);
            responseStreamObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deleteTheater(DeleteTheaterRequest request, StreamObserver<DeleteTheaterResponse> responseObserver) {

        try{
            TheaterService.DeleteRequest deleteRequest = theaterService.deleteTheater(UUID.fromString(request.getTheaterId()));

            switch (deleteRequest) {
                case SUCCESS -> {
                        DeleteTheaterResponse successResponse = DeleteTheaterResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Theater deleted successfully")
                        .build();
                    responseObserver.onNext(successResponse);
                    responseObserver.onCompleted();
                }
                case NOT_FOUND -> responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("Theater not found")
                                .asRuntimeException()
                );
                case HAS_ACTIVE_SHOWS -> responseObserver.onError(
                        Status.FAILED_PRECONDITION
                                .withDescription("Cannot delete theater with active shows")
                                .asRuntimeException()
                );
            }
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid theater ID")
                            .asRuntimeException()
            );
        }
        catch (Exception e) {
            log.error("Error deleting movie", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void addScreen(AddScreenRequest request, StreamObserver<ScreenResponse> responseObserver) {
        try {
            Screen screen = screenMapper.toScreen(request);

            log.info("Adding screen to theater with ID: {} and total seats {} and request total {}",
                    request.getTheaterId(), screen.getTotalSeats(), request.getTotalSeats());
            Screen addedScreen = theaterService.addScreen(UUID.fromString(request.getTheaterId()), screen);

            ScreenResponse screenResponse = screenMapper.toScreenResponse(addedScreen);
            responseObserver.onNext(screenResponse);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid screen ID")
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("Error adding screen", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

        @Override
        public void listScreensByTheater(ListScreensByTheaterRequest request, StreamObserver<ListScreensResponse> responseObserver) {
            try {
                List<Screen> screens = theaterService.listScreensByTheater(UUID.fromString(request.getTheaterId()));

                ListScreensResponse.Builder responseBuilder = ListScreensResponse.newBuilder();
                screens.forEach(screen -> responseBuilder.addScreens(screenMapper.toScreenResponse(screen)));
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
            } catch (IllegalArgumentException e) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Invalid theater ID")
                                .asRuntimeException()
                );
            } catch (Exception e) {
                log.error("Error listing screens by theater", e);
                responseObserver.onError(
                        Status.INTERNAL
                                .withDescription("Internal server error")
                                .withCause(e)
                                .asRuntimeException()
                );
            }
        }

}
