package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.show.*;
import com.microservices.gateway.services.gRPCServices.ShowGRPCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * REST Controller for Show operations
 * Follows RESTful API design principles
 * 
 * Endpoints:
 * - POST   /api/v1/shows                    - Create a show
 * - PUT    /api/v1/shows/{showId}           - Update a show
 * - GET    /api/v1/shows/{showId}           - Get show by ID
 * - GET    /api/v1/shows/movie/{movieId}    - List shows by movie
 * - GET    /api/v1/shows/theater/{theaterId}- List shows by theater
 * - GET    /api/v1/shows/{showId}/seats     - Get available seats
 * - GET    /api/v1/shows/available-slots     - Get available show slots for a screen/date
 * 
 * Design Patterns:
 * - Reactive Programming: Uses Reactor for non-blocking I/O
 * - Dependency Injection: ShowGRPCService injected via constructor
 * - Validation: Jakarta validation on request DTOs
 * 
 * SOLID Principles:
 * - Single Responsibility: Handles only HTTP request/response for shows
 * - Open/Closed: Extensible through additional endpoints
 * - Dependency Inversion: Depends on ShowGRPCService abstraction
 * 
 * Time Complexity: O(1) for single operations, O(n) for list operations
 * Space Complexity: Dominated by response size
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
@Tag(name = "Show Management", description = "APIs for managing movie shows/screenings")
public class ShowController {

    private final ShowGRPCService showGRPCService;

    /**
     * Creates a new show
     * Time Complexity: O(1) + gRPC overhead
     * 
     * @param request CreateShowRequestDTO with show details
     * @return ResponseEntity with created ShowResponseDTO
     */
    @Operation(summary = "Create a new show", 
               description = "Creates a new show/screening for a movie on a specific screen")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Show created successfully",
                    content = @Content(schema = @Schema(implementation = ShowResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request (validation failed, timing conflict)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movie or Screen not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Show timing conflicts with existing show"
            )
    })
    @PostMapping
    public Mono<ResponseEntity<ShowResponseDTO>> createShow(
            @Valid @RequestBody CreateShowRequestDTO request) {
        
        log.info("REST: Received request to create show for movie {} on screen {} at {}", 
                request.movieId(), request.screenId(), request.showDateTime());

        return Mono.fromCallable(() -> showGRPCService.createShow(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnSuccess(response -> log.info("REST: Show created successfully: {}", 
                        response.getBody().showId()))
                .doOnError(error -> log.error("REST: Error creating show", error));
    }

    /**
     * Updates an existing show
     * Time Complexity: O(1) + gRPC overhead
     * 
     * @param showId UUID of the show to update
     * @param request UpdateShowRequestDTO with updated details
     * @return ResponseEntity with updated ShowResponseDTO
     */
    @Operation(summary = "Update a show", 
               description = "Updates an existing show's details (time, price, type)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Show updated successfully",
                    content = @Content(schema = @Schema(implementation = ShowResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request (validation failed, timing conflict)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Show not found"
            )
    })
    @PutMapping("/{showId}")
    public Mono<ResponseEntity<ShowResponseDTO>> updateShow(
            @PathVariable String showId,
            @Valid @RequestBody UpdateShowRequestDTO request) {
        
        log.info("REST: Received request to update show with ID: {}", showId);

        // Ensure showId in path matches request
        UpdateShowRequestDTO updatedRequest = new UpdateShowRequestDTO(
                showId,
                request.showDateTime(),
                request.basePrice(),
                request.showType()
        );

        return Mono.fromCallable(() -> showGRPCService.updateShow(updatedRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Show updated successfully: {}", showId))
                .doOnError(error -> log.error("REST: Error updating show {}", showId, error));
    }

    /**
     * Retrieves a show by ID
     * Time Complexity: O(1) + gRPC overhead
     * 
     * @param showId UUID of the show
     * @return ResponseEntity with ShowResponseDTO
     */
    @Operation(summary = "Get show by ID", 
               description = "Retrieves complete details of a show including movie and screen info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Show retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ShowResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid show ID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Show not found"
            )
    })
    @GetMapping("/{showId}")
    public Mono<ResponseEntity<ShowResponseDTO>> getShow(@PathVariable String showId) {
        log.info("REST: Received request to get show with ID: {}", showId);

        return Mono.fromCallable(() -> showGRPCService.getShow(showId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("REST: Error getting show {}", showId, error));
    }

    @Operation(summary = "List shows with pagination",
               description = "Lists all shows with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shows retrieved successfully",
                         content = @Content(schema = @Schema(implementation = ShowListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")

    })
    @GetMapping("")
    public Mono<ShowListResponseDTO> getShows(@RequestParam int page, @RequestParam int size) {
        log.info("REST: Received request to list shows with pagination - page: {}, size: {}", page, size);
        return Mono.fromCallable(() -> showGRPCService.getShows(page, size))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(error -> log.error("REST: Error listing shows with pagination - page: {}, size: {}", page, size, error));
    }

    /**
     * Lists shows by movie ID with optional filters
     * Time Complexity: O(n) where n is number of matching shows
     * 
     * @param movieId UUID of the movie
     * @param date Optional date filter (YYYY-MM-DD)
     * @param city Optional city filter
     * @return ResponseEntity with ShowListResponseDTO
     */
    @Operation(summary = "List shows by movie", 
               description = "Lists all shows for a specific movie with optional date and city filters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shows retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ShowListResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid movie ID or date format"
            )
    })
    @GetMapping("/movie/{movieId}")
    public Mono<ResponseEntity<ShowListResponseDTO>> listShowsByMovie(
            @PathVariable String movieId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String city) {
        
        log.info("REST: Received request to list shows for movie {} on date {} in city {}", 
                movieId, date, city);

        return Mono.fromCallable(() -> showGRPCService.listShowsByMovie(movieId, date, city))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Found {} shows for movie {}", 
                        response.getBody().totalCount(), movieId))
                .doOnError(error -> log.error("REST: Error listing shows for movie {}", 
                        movieId, error));
    }

    /**
     * Lists shows by theater ID with optional date filter
     * Time Complexity: O(n) where n is number of matching shows
     * 
     * @param theaterId UUID of the theater
     * @param date Optional date filter (YYYY-MM-DD)
     * @return ResponseEntity with ShowListResponseDTO
     */
    @Operation(summary = "List shows by theater", 
               description = "Lists all shows for a specific theater with optional date filter")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shows retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ShowListResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid theater ID or date format"
            )
    })
    @GetMapping("/theater/{theaterId}")
    public Mono<ResponseEntity<ShowListResponseDTO>> listShowsByTheater(
            @PathVariable String theaterId,
            @RequestParam(required = false) String date) {
        
        log.info("REST: Received request to list shows for theater {} on date {}", 
                theaterId, date);

        return Mono.fromCallable(() -> showGRPCService.listShowsByTheater(theaterId, date))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Found {} shows for theater {}", 
                        response.getBody().totalCount(), theaterId))
                .doOnError(error -> log.error("REST: Error listing shows for theater {}", 
                        theaterId, error));
    }

    /**
     * Retrieves available seats for a show
     * Time Complexity: O(m) where m is total number of seats
     * 
     * @param showId UUID of the show
     * @return ResponseEntity with AvailableSeatsResponseDTO
     */
    @Operation(summary = "Get available seats for a show", 
               description = "Retrieves all seats with availability status (AVAILABLE, LOCKED, BOOKED)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seat availability retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AvailableSeatsResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid show ID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Show not found"
            )
    })
    @GetMapping("/{showId}/seats")
    public Mono<ResponseEntity<AvailableSeatsResponseDTO>> getAvailableSeats(
            @PathVariable String showId) {
        
        log.info("REST: Received request to get available seats for show {}", showId);

        return Mono.fromCallable(() -> showGRPCService.getAvailableSeats(showId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Found {} available seats for show {}", 
                        response.getBody().totalAvailable(), showId))
                .doOnError(error -> log.error("REST: Error getting available seats for show {}", 
                        showId, error));
    }

    /**
     * Retrieves all available show slots for the requested date on a screen.
     * Time Complexity: O(n) where n is number of computed available slots
     *
     * @param request AvailableShowTimesRequestDTO containing theater, screen, runtime and requested datetime
     * @return ResponseEntity with AvailableShowTimesResponseDTO
     */
    @Operation(summary = "Get available show slots",
               description = "Returns all available show slots for the requested date on a screen after validating that the screen belongs to the theater")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Available slots retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AvailableShowTimesResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request (invalid IDs, runtime, datetime format, or screen-theater mismatch)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Screen not found"
            )
    })
    @PostMapping("/available-slots")
    public Mono<ResponseEntity<AvailableShowTimesResponseDTO>> getAvailableShowSlots(
            @Valid @RequestBody AvailableShowTimesRequestDTO request) {

        log.info("REST: Received request to get available slots for theater {}, screen {}, runtime {}, requested {}",
                request.theaterId(), request.screenId(), request.movieRunTime(), request.requestedShowDateTime());

        return Mono.fromCallable(() -> showGRPCService.getAvailableShowTimes(request))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("REST: Found {} available slots for screen {} on date {}",
                        response.getBody().totalCount(), request.screenId(), response.getBody().requestedDate()))
                .doOnError(error -> log.error("REST: Error getting available slots for screen {}",
                        request.screenId(), error));
    }
}
