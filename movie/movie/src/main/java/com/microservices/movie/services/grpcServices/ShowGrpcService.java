package com.microservices.movie.services.grpcServices;

import com.microservices.movie.grpc.*;
import com.microservices.movie.mappers.MovieMapper;
import com.microservices.movie.mappers.ScreenMapper;
import com.microservices.movie.mappers.ShowMapper;
import com.microservices.movie.models.entities.Show;
import com.microservices.movie.models.enums.SeatStatus;
import com.microservices.movie.services.interfaces.ShowService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * gRPC service implementation for Show operations
 * Follows SOLID principles with clear separation of concerns
 * 
 * Time Complexity Analysis:
 * - createShow: O(1) database insert + O(n) validation for overlapping shows
 * - updateShow: O(1) database update + O(n) validation
 * - getShow: O(1) indexed database lookup
 * - listShowsByMovie: O(n) where n is number of matching shows
 * - listShowsByTheater: O(n) where n is number of matching shows
 * - getAvailableSeats: O(m) where m is total seats in the screen
 * 
 * Space Complexity: O(1) for single operations, O(n) for list operations
 * 
 * Design Patterns:
 * - Dependency Injection: All dependencies injected via constructor
 * - Error Handling: Comprehensive exception mapping to gRPC status codes
 * - Logging: Structured logging for observability
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ShowGrpcService extends ShowServiceGrpc.ShowServiceImplBase {

    private final ShowService showService;
    private final ShowMapper showMapper;
    private final MovieMapper movieMapper;
    private final ScreenMapper screenMapper;

    /**
     * Creates a new show
     * Time Complexity: O(1) database insert + O(n) overlap validation
     * Space Complexity: O(1)
     * 
     * @param request CreateShowRequest containing show details
     * @param responseObserver StreamObserver to send response
     */
    @Override
    public void createShow(CreateShowRequest request, StreamObserver<ShowResponse> responseObserver) {
        log.info("gRPC: Creating show for movie {} on screen {} at {} on theater {}",
                request.getMovieId(), request.getScreenId(), request.getShowDateTime(), request.getTheaterId());

        try {
            validateCreateShowRequest(request);

            Show show = showMapper.toShow(request);

            Show createdShow = showService.createShow(show);

            ShowResponse response = showMapper.toShowResponse(createdShow, movieMapper, screenMapper);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.info("gRPC: Show created successfully with ID: {}", createdShow.getId());

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid argument for creating show", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.ResourceNotFoundException e) {
            log.error("gRPC: Resource not found while creating show", e);
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.ResourceAlreadyExistsException e) {
            log.error("gRPC: Show timing conflict", e);
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.InvalidRequestException e) {
            log.error("gRPC: Invalid request", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error creating show", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while creating show")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Updates an existing show
     * Time Complexity: O(1) database update + O(n) validation
     * Space Complexity: O(1)
     * 
     * @param request UpdateShowRequest containing updated show details
     * @param responseObserver StreamObserver to send response
     */
    @Override
    public void updateShow(UpdateShowRequest request, StreamObserver<ShowResponse> responseObserver) {
        log.info("gRPC: Updating show with ID: {}", request.getShowId());

        try {
            UUID showId = UUID.fromString(request.getShowId());
            Show updateData = showMapper.toShow(request);

            Show updatedShow = showService.updateShow(showId, updateData);
            ShowResponse response = showMapper.toShowResponse(updatedShow, movieMapper, screenMapper);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.info("gRPC: Show updated successfully: {}", showId);

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid show ID format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid show ID format")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.ResourceNotFoundException e) {
            log.error("gRPC: Show not found", e);
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.InvalidRequestException e) {
            log.error("gRPC: Invalid request", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error updating show", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while updating show")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Retrieves a single show by ID
     * Time Complexity: O(1) indexed database lookup
     * Space Complexity: O(1)
     * 
     * @param request GetShowRequest containing show ID
     * @param responseObserver StreamObserver to send response
     */
    @Transactional(readOnly = true)
    @Override
    public void getShow(GetShowRequest request, StreamObserver<ShowResponse> responseObserver) {
        log.info("gRPC: Getting show with ID: {}", request.getShowId());

        try {
            UUID showId = UUID.fromString(request.getShowId());
            Show show = showService.getShow(showId);

            ShowResponse response = showMapper.toShowResponse(show, movieMapper, screenMapper);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid show ID format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid show ID format")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.ResourceNotFoundException e) {
            log.error("gRPC: Show not found", e);
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error retrieving show", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while retrieving show")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Lists shows by movie ID with optional date and city filtering
     * Time Complexity: O(n) where n is number of matching shows
     * Space Complexity: O(n) for response list
     * Uses indexed query for optimal performance
     * 
     * @param request ListShowsByMovieRequest with movie ID, optional date and city
     * @param responseObserver StreamObserver to send response
     */
    @Transactional(readOnly = true)
    @Override
    public void listShowsByMovie(ListShowsByMovieRequest request, StreamObserver<ListShowsResponse> responseObserver) {
        log.info("gRPC: Listing shows for movie {} on date {} in city {}",
                request.getMovieId(), request.getDate(), request.getCity());

        try {
            UUID movieId = UUID.fromString(request.getMovieId());
            List<Show> shows;

            // Use optimized filtering with indexed queries
            if (!request.getDate().isEmpty() && !request.getDate().isBlank() &&
                    !request.getCity().isEmpty() && !request.getCity().isBlank()) {
                LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);
                shows = showService.listShowsByMovie(movieId).stream()
                        .filter(show -> show.getShowDateTime().toLocalDate().equals(date))
                        .filter(show -> show.getScreen().getTheater().getCity().equalsIgnoreCase(request.getCity()))
                        .toList();
            } else if (!request.getDate().isEmpty() && !request.getDate().isBlank()) {
                LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);
                shows = showService.listShowsByMovie(movieId).stream()
                        .filter(show -> show.getShowDateTime().toLocalDate().equals(date))
                        .toList();
            } else {
                shows = showService.listShowsByMovie(movieId);
            }

            ListShowsResponse.Builder responseBuilder = ListShowsResponse.newBuilder();
            shows.forEach(show -> responseBuilder.addShows(
                    showMapper.toShowResponse(show, movieMapper, screenMapper)));
            responseBuilder.setTotalCount(shows.size());

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid movie ID format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid movie ID or date format")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error listing shows by movie", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while listing shows")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Lists shows by theater ID with optional date filtering
     * Time Complexity: O(n) where n is number of matching shows
     * Space Complexity: O(n) for response list
     * Uses indexed query for optimal performance
     * 
     * @param request ListShowsByTheaterRequest with theater ID and optional date
     * @param responseObserver StreamObserver to send response
     */
    @Transactional(readOnly = true)
    @Override
    public void listShowsByTheater(ListShowsByTheaterRequest request, StreamObserver<ListShowsResponse> responseObserver) {
        log.info("gRPC: Listing shows for theater {} on date {}",
                request.getTheaterId(), request.getDate());

        try {
            UUID theaterId = UUID.fromString(request.getTheaterId());
            List<Show> shows;

            if (!request.getDate().isEmpty() && !request.getDate().isBlank()) {
                LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);
                shows = showService.listShowsByTheater(theaterId).stream()
                        .filter(show -> show.getShowDateTime().toLocalDate().equals(date))
                        .toList();
            } else {
                shows = showService.listShowsByTheater(theaterId);
            }

            ListShowsResponse.Builder responseBuilder = ListShowsResponse.newBuilder();
            shows.forEach(show -> responseBuilder.addShows(
                    showMapper.toShowResponse(show, movieMapper, screenMapper)));
            responseBuilder.setTotalCount(shows.size());

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid theater ID format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid theater ID or date format")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error listing shows by theater", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while listing shows")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Retrieves available seats for a show
     * Time Complexity: O(m) where m is total seats in the screen
     * Space Complexity: O(m) for seat list
     * Includes seat availability calculation with locked/booked status
     * 
     * @param request GetAvailableSeatsRequest containing show ID
     * @param responseObserver StreamObserver to send response
     */
    @Override
    public void getAvailableSeats(GetAvailableSeatsRequest request, StreamObserver<AvailableSeatsResponse> responseObserver) {
        log.info("gRPC: Getting available seats for show {}", request.getShowId());

        try {
            UUID showId = UUID.fromString(request.getShowId());
            List<ShowService.SeatAvailability> availableSeats = showService.getAvailableSeats(showId);

            AvailableSeatsResponse.Builder responseBuilder = AvailableSeatsResponse.newBuilder();
            responseBuilder.setShowId(request.getShowId());

            int totalAvailable = 0;
            for (ShowService.SeatAvailability seatAvail : availableSeats) {
                SeatInfo seatInfo = SeatInfo.newBuilder()
                        .setSeatId(seatAvail.seatId().toString())
                        .setRowName(seatAvail.rowName())
                        .setSeatNumber(seatAvail.seatNumber())
                        .setSeatType(seatAvail.seatType().name())
                        .setPrice(seatAvail.price())
                        .setStatus(seatAvail.status().name())
                        .build();

                responseBuilder.addSeats(seatInfo);

                if (seatAvail.status() == SeatStatus.AVAILABLE) {
                    totalAvailable++;
                }
            }

            responseBuilder.setTotalAvailable(totalAvailable);

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid show ID format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid show ID format")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.ResourceNotFoundException e) {
            log.error("gRPC: Show not found", e);
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error getting available seats", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while getting available seats")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Transactional
    @Override
    public void listShows(ListShowsRequest request, StreamObserver<ListShowsResponse> responseObserver) {
        log.info("gRPC: Listing shows with pagination - page: {}, size: {}", request.getPage(), request.getSize());

        try {
            int page = request.getPage() >= 0 ? request.getPage() : 1;
            int size = request.getSize() > 0 ? request.getSize() : 10;

            var showsPage = showService.listShows(page, size);
            log.info("gRPC: Retrieved {} shows", showsPage.getContent());
            ListShowsResponse.Builder responseBuilder = ListShowsResponse.newBuilder();
            showsPage.forEach(show -> responseBuilder.addShows(
                    showMapper.toShowResponse(show, movieMapper, screenMapper)));
            responseBuilder.setTotalCount((int) showsPage.getTotalElements());
            responseBuilder.setTotalPages( showsPage.getTotalPages() );
            responseBuilder.setHasNext( showsPage.hasNext() );

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("gRPC: Error listing shows", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while listing shows")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void searchShows(SearchShowsRequest request, StreamObserver<ListShowsResponse> responseObserver) {
        log.info("gRPC: Searching shows movieId={}, theaterId={}, date={}, city={}, showType={}, genre={}, language={}, page={}, size={}",
                request.getMovieId(), request.getTheaterId(), request.getDate(), request.getCity(), request.getShowType(), request.getGenre(), request.getLanguage(), request.getPage(), request.getSize());

        try {
            UUID movieId = request.getMovieId().isBlank() ? null : UUID.fromString(request.getMovieId());
            UUID theaterId = request.getTheaterId().isBlank() ? null : UUID.fromString(request.getTheaterId());
            LocalDate date = request.getDate().isBlank()
                    ? null
                    : LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);

            Page<Show> showPage = showService.searchShows(
                    movieId,
                    theaterId,
                    date,
                    request.getCity(),
                    request.getShowType(),
                    request.getGenre(),
                    request.getLanguage(),
                    request.getPage(),
                    request.getSize()
            );

            ListShowsResponse.Builder responseBuilder = ListShowsResponse.newBuilder();
            showPage.forEach(show -> responseBuilder.addShows(
                    showMapper.toShowResponse(show, movieMapper, screenMapper)));
            responseBuilder.setTotalCount((int) showPage.getTotalElements());
            responseBuilder.setTotalPages(showPage.getTotalPages());
            responseBuilder.setHasNext(showPage.hasNext());
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (com.microservices.movie.exceptions.InvalidRequestException e) {
            log.error("gRPC: Invalid search filters", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (java.time.format.DateTimeParseException e) {
            log.error("gRPC: Invalid search date format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid date format. Expected yyyy-MM-dd")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid search parameters", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid search parameters")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error searching shows", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while searching shows")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Retrieves available show time slots for a screen on a specific date.
     */
    @Override
    public void getAvailableShowTimes(GetAvailableShowTimesRequest request,
                                      StreamObserver<AvailableShowTimesResponse> responseObserver) {
        log.info("gRPC: Getting available show times for theater {}, screen {}, requested datetime {}",
                request.getTheaterId(), request.getScreenId(), request.getRequestedShowDateTime());

        try {
            UUID theaterId = UUID.fromString(request.getTheaterId());
            UUID screenId = UUID.fromString(request.getScreenId());
            LocalDateTime requestedShowDateTime = LocalDateTime.parse(
                    request.getRequestedShowDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDate date = requestedShowDateTime.toLocalDate();

            List<ShowService.ShowTimeAvailability> availableSlots = showService.getAvailableShowTimesForDate(
                    date,
                    theaterId,
                    screenId,
                    request.getMovieRuntimeMinutes()
            );

            AvailableShowTimesResponse.Builder responseBuilder = AvailableShowTimesResponse.newBuilder()
                    .setTheaterId(request.getTheaterId())
                    .setScreenId(request.getScreenId())
                    .setRequestedDate(date.toString())
                    .setTotalCount(availableSlots.size());

            availableSlots.forEach(slot -> responseBuilder.addSlots(
                    ShowTimeSlot.newBuilder()
                            .setStartTime(slot.startTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                            .setEndTime(slot.endTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                            .build()
            ));

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.error("gRPC: Invalid theater/screen ID format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid theater ID or screen ID format")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (java.time.format.DateTimeParseException e) {
            log.error("gRPC: Invalid requested show datetime format", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid requestedShowDateTime format. Expected yyyy-MM-ddTHH:mm:ss")
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.InvalidRequestException e) {
            log.error("gRPC: Invalid request for available show times", e);
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (com.microservices.movie.exceptions.ResourceNotFoundException e) {
            log.error("gRPC: Resource not found while getting available show times", e);
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        } catch (Exception e) {
            log.error("gRPC: Error getting available show times", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error while getting available show times")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    /**
     * Validates create show request
     * Time Complexity: O(1)
     * 
     * @param request CreateShowRequest to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCreateShowRequest(CreateShowRequest request) {
        if (request.getMovieId() == null || request.getMovieId().isBlank()) {
            throw new IllegalArgumentException("Movie ID is required");
        }
        if (request.getScreenId() == null || request.getScreenId().isBlank()) {
            throw new IllegalArgumentException("Screen ID is required");
        }
        if (request.getShowDateTime() == null || request.getShowDateTime().isBlank()) {
            throw new IllegalArgumentException("Show date time is required");
        }
        if (request.getBasePrice() <= 0) {
            throw new IllegalArgumentException("Base price must be greater than zero");
        }
        if (request.getShowType() == null || request.getShowType().isBlank()) {
            throw new IllegalArgumentException("Show type is required");
        }
    }
}
