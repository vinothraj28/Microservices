package com.microservices.gateway.services.gRPCServices;

import com.google.protobuf.Timestamp;
import com.microservices.gateway.DTOS.movie.MovieResponseDTO;
import com.microservices.gateway.DTOS.screen.ScreenResponseDTO;
import com.microservices.gateway.DTOS.screen.SeatLayoutResponseDTO;
import com.microservices.gateway.DTOS.show.*;
import com.microservices.movie.grpc.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gateway gRPC client service for Show operations
 * Handles communication between Gateway REST API and Movie Service gRPC server
 * 
 * Design Patterns:
 * - Adapter Pattern: Converts between DTOs and gRPC proto messages
 * - Error Handling: Maps gRPC status codes to domain exceptions
 * 
 * Time Complexity: O(1) for single operations, O(n) for list operations
 * Space Complexity: O(1) for single operations, O(n) for collections
 * 
 * SOLID Principles:
 * - Single Responsibility: Handles only gRPC communication for shows
 * - Dependency Inversion: Depends on gRPC stub abstraction
 */
@Slf4j
@Service
public class ShowGRPCService {

    @GrpcClient("movie-service")
    private ShowServiceGrpc.ShowServiceBlockingStub showServiceStub;

    /**
     * Creates a new show via gRPC
     * Time Complexity: O(1)
     * 
     * @param request CreateShowRequestDTO containing show details
     * @return ShowResponseDTO with created show information
     * @throws RuntimeException if gRPC call fails
     */
    public ShowResponseDTO createShow(CreateShowRequestDTO request) {
        log.info("Gateway gRPC: Creating show for movie {} on screen {} on theater {}",
                request.movieId(), request.screenId(), request.theaterId());

        try {
            CreateShowRequest grpcRequest = CreateShowRequest.newBuilder()
                    .setMovieId(request.movieId())
                    .setScreenId(request.screenId())
                    .setTheaterId(request.theaterId())
                    .setShowDateTime(request.showDateTime())
                    .setBasePrice(request.basePrice())
                    .setShowType(request.showType())
                    .build();

            ShowResponse response = showServiceStub.createShow(grpcRequest);
            return mapToShowResponseDTO(response);

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error creating show: {}", e.getStatus().getDescription(), e);
            handleGrpcException(e, "creating show");
            throw e; // Never reached due to handleGrpcException throwing
        }
    }

    /**
     * Updates an existing show via gRPC
     * Time Complexity: O(1)
     * 
     * @param request UpdateShowRequestDTO containing updated show details
     * @return ShowResponseDTO with updated show information
     * @throws RuntimeException if gRPC call fails
     */
    public ShowResponseDTO updateShow(UpdateShowRequestDTO request) {
        log.info("Gateway gRPC: Updating show with ID: {}", request.showId());

        try {
            UpdateShowRequest.Builder grpcRequestBuilder = UpdateShowRequest.newBuilder()
                    .setShowId(request.showId());

            if (request.showDateTime() != null) {
                grpcRequestBuilder.setShowDateTime(request.showDateTime());
            }
            if (request.basePrice() != null) {
                grpcRequestBuilder.setBasePrice(request.basePrice());
            }
            if (request.showType() != null) {
                grpcRequestBuilder.setShowType(request.showType());
            }

            ShowResponse response = showServiceStub.updateShow(grpcRequestBuilder.build());
            return mapToShowResponseDTO(response);

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error updating show: {}", e.getStatus().getDescription(), e);
            handleGrpcException(e, "updating show");
            throw e;
        }
    }

    /**
     * Retrieves a show by ID via gRPC
     * Time Complexity: O(1)
     * 
     * @param showId UUID of the show
     * @return ShowResponseDTO with show information
     * @throws RuntimeException if gRPC call fails
     */
    public ShowResponseDTO getShow(String showId) {
        log.info("Gateway gRPC: Getting show with ID: {}", showId);

        try {
            GetShowRequest request = GetShowRequest.newBuilder()
                    .setShowId(showId)
                    .build();

            ShowResponse response = showServiceStub.getShow(request);
            return mapToShowResponseDTO(response);

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error getting show: {}", e.getStatus().getDescription(), e);
            handleGrpcException(e, "getting show");
            throw e;
        }
    }

    public ShowListResponseDTO getShows(int page, int size){
        log.info("Gateway gRPC: Getting shows with pagination - page: {}, size: {}", page, size);

        try{
            ListShowsRequest listShowsRequest = ListShowsRequest.newBuilder()
                    .setPage(page)
                    .setSize(size)
                    .build();

            ListShowsResponse shows = showServiceStub.listShows(listShowsRequest);
            return ShowListResponseDTO.from(shows.getShowsList().stream()
                    .map(this::mapToShowResponseDTO)
                    .collect(Collectors.toList()), shows.getTotalCount(), shows.getTotalPages(), shows.getHasNext());
        }catch (StatusRuntimeException e){
            log.error("Gateway gRPC: Error getting shows with pagination: {}", e.getStatus().getDescription(), e);
            handleGrpcException(e, "getting shows with pagination");
            throw e;
        }

    }

    /**
     * Lists shows by movie ID with optional filters
     * Time Complexity: O(n) where n is number of matching shows
     * 
     * @param movieId UUID of the movie
     * @param date Optional date filter (YYYY-MM-DD)
     * @param city Optional city filter
     * @return ShowListResponseDTO with list of shows
     * @throws RuntimeException if gRPC call fails
     */
    public ShowListResponseDTO listShowsByMovie(String movieId, String date, String city) {
        log.info("Gateway gRPC: Listing shows for movie {} on date {} in city {}", 
                movieId, date, city);

        try {
            ListShowsByMovieRequest.Builder requestBuilder = ListShowsByMovieRequest.newBuilder()
                    .setMovieId(movieId);

            if (date != null && !date.isBlank()) {
                requestBuilder.setDate(date);
            }
            if (city != null && !city.isBlank()) {
                requestBuilder.setCity(city);
            }

            ListShowsResponse response = showServiceStub.listShowsByMovie(requestBuilder.build());

            List<ShowResponseDTO> shows = response.getShowsList().stream()
                    .map(this::mapToShowResponseDTO)
                    .collect(Collectors.toList());

            return ShowListResponseDTO.fromShowsAndTotalCount(shows, response.getTotalCount());

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error listing shows by movie: {}", 
                    e.getStatus().getDescription(), e);
            handleGrpcException(e, "listing shows by movie");
            throw e;
        }
    }

    /**
     * Lists shows by theater ID with optional date filter
     * Time Complexity: O(n) where n is number of matching shows
     * 
     * @param theaterId UUID of the theater
     * @param date Optional date filter (YYYY-MM-DD)
     * @return ShowListResponseDTO with list of shows
     * @throws RuntimeException if gRPC call fails
     */
    public ShowListResponseDTO listShowsByTheater(String theaterId, String date) {
        log.info("Gateway gRPC: Listing shows for theater {} on date {}", theaterId, date);

        try {
            ListShowsByTheaterRequest.Builder requestBuilder = ListShowsByTheaterRequest.newBuilder()
                    .setTheaterId(theaterId);

            if (date != null && !date.isBlank()) {
                requestBuilder.setDate(date);
            }

            ListShowsResponse response = showServiceStub.listShowsByTheater(requestBuilder.build());

            List<ShowResponseDTO> shows = response.getShowsList().stream()
                    .map(this::mapToShowResponseDTO)
                    .collect(Collectors.toList());

            return new ShowListResponseDTO(shows, response.getTotalCount(), response.getTotalPages(), response.getHasNext());

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error listing shows by theater: {}", 
                    e.getStatus().getDescription(), e);
            handleGrpcException(e, "listing shows by theater");
            throw e;
        }
    }

    public ShowListResponseDTO searchShows(
            String movieId,
            String theaterId,
            String date,
            String city,
            String showType,
            String genre,
            String language,
            int page,
            int size
    ) {
        log.info("Gateway gRPC: Searching shows movieId={}, theaterId={}, date={}, city={}, showType={}, genre={}, language={}, page={}, size={}",
                movieId, theaterId, date, city, showType, genre, language, page, size);

        try {
            SearchShowsRequest request = SearchShowsRequest.newBuilder()
                    .setMovieId(movieId == null ? "" : movieId)
                    .setTheaterId(theaterId == null ? "" : theaterId)
                    .setDate(date == null ? "" : date)
                    .setCity(city == null ? "" : city)
                    .setShowType(showType == null ? "" : showType)
                    .setGenre(genre == null ? "" : genre)
                    .setLanguage(language == null ? "" : language)
                    .setPage(page)
                    .setSize(size)
                    .build();

            ListShowsResponse response = showServiceStub.searchShows(request);
            List<ShowResponseDTO> shows = response.getShowsList().stream()
                    .map(this::mapToShowResponseDTO)
                    .collect(Collectors.toList());

            return ShowListResponseDTO.from(shows, response.getTotalCount(), response.getTotalPages(), response.getHasNext());
        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error searching shows: {}", e.getStatus().getDescription(), e);
            handleGrpcException(e, "searching shows");
            throw e;
        }
    }

    /**
     * Retrieves available seats for a show
     * Time Complexity: O(m) where m is number of seats
     * 
     * @param showId UUID of the show
     * @return AvailableSeatsResponseDTO with seat availability information
     * @throws RuntimeException if gRPC call fails
     */
    public AvailableSeatsResponseDTO getAvailableSeats(String showId) {
        log.info("Gateway gRPC: Getting available seats for show {}", showId);

        try {
            GetAvailableSeatsRequest request = GetAvailableSeatsRequest.newBuilder()
                    .setShowId(showId)
                    .build();

            AvailableSeatsResponse response = showServiceStub.getAvailableSeats(request);

            List<SeatInfoDTO> seats = response.getSeatsList().stream()
                    .map(seatInfo -> new SeatInfoDTO(
                            seatInfo.getSeatId(),
                            seatInfo.getRowName(),
                            seatInfo.getSeatNumber(),
                            seatInfo.getSeatType(),
                            seatInfo.getPrice(),
                            seatInfo.getStatus(),
                            seatInfo.getLockedUntil()
                    ))
                    .collect(Collectors.toList());

            return new AvailableSeatsResponseDTO(
                    response.getShowId(),
                    seats,
                    response.getTotalAvailable()
            );

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error getting available seats: {}", 
                    e.getStatus().getDescription(), e);
            handleGrpcException(e, "getting available seats");
            throw e;
        }
    }

    /**
     * Retrieves available show time slots for a screen on a requested date.
     * Time Complexity: O(n) where n is number of available slots
     *
     * @param request AvailableShowTimesRequestDTO with theater, screen, runtime and requested datetime
     * @return AvailableShowTimesResponseDTO with available slots
     * @throws RuntimeException if gRPC call fails
     */
    public AvailableShowTimesResponseDTO getAvailableShowTimes(AvailableShowTimesRequestDTO request) {
        log.info("Gateway gRPC: Getting available show times for theater {}, screen {}, requested datetime {}",
                request.theaterId(), request.screenId(), request.requestedShowDateTime());

        try {
            GetAvailableShowTimesRequest grpcRequest = GetAvailableShowTimesRequest.newBuilder()
                    .setTheaterId(request.theaterId())
                    .setScreenId(request.screenId())
                    .setMovieRuntimeMinutes(request.movieRunTime())
                    .setRequestedShowDateTime(request.requestedShowDateTime())
                    .build();

            AvailableShowTimesResponse response = showServiceStub.getAvailableShowTimes(grpcRequest);

            List<ShowTimeSlotDTO> slots = response.getSlotsList().stream()
                    .map(slot -> new ShowTimeSlotDTO(slot.getStartTime(), slot.getEndTime()))
                    .collect(Collectors.toList());

            return new AvailableShowTimesResponseDTO(
                    response.getTheaterId(),
                    response.getScreenId(),
                    response.getRequestedDate(),
                    slots,
                    response.getTotalCount()
            );

        } catch (StatusRuntimeException e) {
            log.error("Gateway gRPC: Error getting available show times: {}",
                    e.getStatus().getDescription(), e);
            handleGrpcException(e, "getting available show times");
            throw e;
        }
    }

    /**
     * Maps gRPC ShowResponse to ShowResponseDTO
     * Time Complexity: O(1)
     * 
     * @param response gRPC ShowResponse
     * @return ShowResponseDTO
     */
    private ShowResponseDTO mapToShowResponseDTO(ShowResponse response) {
        MovieResponseDTO movie = mapToMovieResponseDTO(response.getMovie());
        ScreenResponseDTO screen = mapToScreenResponseDTO(response.getScreen());

        log.info("Mapping ShowResponse to ShowResponseDTO for showId: {}, movieId: {}, screenId: {}",
                response.getShowId(), response.getMovieId(), response.getScreenId());

        LocalDateTime createdAt = response.hasCreatedAt() 
                ? toLocalDateTime(response.getCreatedAt()) 
                : null;
        LocalDateTime updatedAt = response.hasUpdatedAt() 
                ? toLocalDateTime(response.getUpdatedAt()) 
                : null;

        return new ShowResponseDTO(
                response.getShowId(),
                response.getMovieId(),
                response.getScreenId(),
                response.getTheaterId(),
                movie,
                screen,
                response.getShowDateTime(),
                response.getBasePrice(),
                response.getShowType(),
                response.getAvailableSeats(),
                createdAt,
                updatedAt
        );
    }

    /**
     * Maps gRPC MovieResponse to MovieResponseDTO
     * Time Complexity: O(1)
     */
    private MovieResponseDTO mapToMovieResponseDTO(MovieResponse response) {
        if (response == null || response.getMovieId().isEmpty()) {
            return null;
        }

        LocalDateTime createdAt = response.hasCreatedAt() 
                ? toLocalDateTime(response.getCreatedAt()) 
                : null;
        LocalDateTime updatedAt = response.hasUpdatedAt() 
                ? toLocalDateTime(response.getUpdatedAt()) 
                : null;

        return new MovieResponseDTO(
                response.getMovieId(),
                response.getTitle(),
                response.getDescription(),
                response.getDurationMinutes(),
                response.getGenre(),
                response.getLanguage(),
                response.getReleaseDate(),
                response.getPosterUrl(),
                response.getTrailerUrl(),
                response.getRating(),
                response.getCastList(),
                response.getCrewList(),
                createdAt,
                updatedAt,
                response.getImageId()
        );
    }

    /**
     * Maps gRPC ScreenResponse to ScreenResponseDTO
     * Time Complexity: O(n) where n is number of seat layouts
     */
    private ScreenResponseDTO mapToScreenResponseDTO(ScreenResponse response) {
        if (response == null || response.getScreenId().isEmpty()) {
            return null;
        }

        List<SeatLayoutResponseDTO> seatLayouts = response.getSeatLayoutList().stream()
                .map(layout -> new SeatLayoutResponseDTO(
                        layout.getRowName(),
                        layout.getStartSeatNumber(),
                        layout.getEndSeatNumber(),
                        layout.getSeatType(),
                        layout.getPriceMultiplier()
                ))
                .collect(Collectors.toList());

        LocalDateTime createdAt = response.hasCreatedAt() 
                ? toLocalDateTime(response.getCreatedAt()) 
                : null;
        LocalDateTime updatedAt = response.hasUpdatedAt() 
                ? toLocalDateTime(response.getUpdatedAt()) 
                : null;

        return new ScreenResponseDTO(
                response.getScreenId(),
                response.getTheaterId(),
                response.getScreenName(),
                response.getScreenNumber(),
                response.getTotalSeats(),
                response.getScreenType(),
                seatLayouts,
                createdAt,
                updatedAt
        );
    }

    /**
     * Converts gRPC Timestamp to LocalDateTime
     * Time Complexity: O(1)
     */
    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    /**
     * Handles gRPC exceptions and maps them to appropriate runtime exceptions
     * Time Complexity: O(1)
     * 
     * @param e StatusRuntimeException from gRPC
     * @param operation Description of the operation that failed
     * @throws RuntimeException with appropriate message
     */
    private void handleGrpcException(StatusRuntimeException e, String operation) {
        Status.Code code = e.getStatus().getCode();
        String description = e.getStatus().getDescription();

        switch (code) {
            case NOT_FOUND:
                throw new RuntimeException("Resource not found: " + description, e);
            case ALREADY_EXISTS:
                throw new RuntimeException("Resource already exists: " + description, e);
            case INVALID_ARGUMENT:
                throw new IllegalArgumentException("Invalid argument: " + description, e);
            case PERMISSION_DENIED:
                throw new RuntimeException("Permission denied: " + description, e);
            case UNAUTHENTICATED:
                throw new RuntimeException("Authentication required: " + description, e);
            default:
                throw new RuntimeException("Error " + operation + ": " + description, e);
        }
    }
}
