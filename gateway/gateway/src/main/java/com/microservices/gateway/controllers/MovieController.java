package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.auth.AuthenticationResponseDTO;
import com.microservices.gateway.DTOS.movie.*;
import com.microservices.gateway.services.gRPCServices.ImageGrpcService;
import com.microservices.gateway.services.gRPCServices.MovieGrpcService;
import com.microservices.movie.grpc.ListMoviesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieGrpcService movieGrpcService;
    private final ImageGrpcService imageGrpcService;

    public MovieController(MovieGrpcService movieGrpcService, ImageGrpcService imageGrpcService) {
        this.movieGrpcService = movieGrpcService;
        this.imageGrpcService = imageGrpcService;
    }

    @Operation(summary = "Create a new movie", description = "Creates a new movie in the movie service")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Movie created successfully",
                    content = @Content(schema = @Schema(implementation = MovieResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Validation failed (invalid movie request format, blank fields)"
            )
    })
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<MovieResponseDTO>> create(@Valid @RequestPart MovieRequestDTO movieRequestDTO,
                                                         @RequestPart(required = false) FilePart movieImage) {

        log.info("Received request to create movie with title: {}", movieRequestDTO.title());

        MediaType mediaType = movieImage.headers().getContentType();

        log.info("Uploaded content type: {}", mediaType);

        return Mono.fromCallable(() -> movieGrpcService.create(movieRequestDTO, movieImage))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(movieResponseDTO -> Mono.just(ResponseEntity.ok(movieResponseDTO)));

    }

    @Operation(summary = "Update an existing movie", description = "Updates an existing movie in the movie service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Movie updated successfully",
                    content = @Content(schema = @Schema(implementation = MovieResponseDTO.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized access"),
            @ApiResponse(responseCode = "422",
                    description = "Validation failed (invalid movie update request format, blank fields)")
    })
    @PutMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<MovieResponseDTO>> update(@Valid @RequestPart MovieUpdateRequestDTO movieUpdateRequestDTO,
                                                         @RequestPart(required = false) FilePart movieImage) {
        log.info("Received request to update movie with title: {}", movieUpdateRequestDTO.title());

        MediaType mediaType = movieImage.headers().getContentType();

        log.info("Uploaded content type: {}", mediaType);

        return Mono.fromCallable(() -> movieGrpcService.updateMovie(movieUpdateRequestDTO, movieImage))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Get a movie by ID", description = "Retrieves a movie by its ID from the movie service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Movie retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MovieResponseDTO.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized access"),
            @ApiResponse(responseCode = "404",
                    description = "Movie not found")
    })
    @GetMapping("/{movieId}")
    public Mono<ResponseEntity<MovieResponseDTO>> getMovie(@PathVariable String movieId) {
        return Mono.fromCallable(() -> movieGrpcService.getMovie(movieId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/")
    public Mono<ResponseEntity<MovieListResponseDTO>> listMovies(@RequestParam int page, @RequestParam int size,
                                                               @RequestParam(required = false) String genre, @RequestParam(required = false) String language) {
        log.info("Received request to list movies with page: {}, size: {}, genre: {}, language: {}"
                , page, size, genre, language);
        return Mono.fromCallable(() -> movieGrpcService.listMovies(page, size, genre, language))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(listMoviesResponse -> {
                    List<MovieResponseDTO> movieResponseDTOs = listMoviesResponse.getMoviesList().stream()
                            .map(movie ->
                                    new MovieResponseDTO(
                                            movie.getMovieId(),
                                            movie.getTitle(),
                                            movie.getDescription(),
                                            movie.getDurationMinutes(),
                                            movie.getGenre(),
                                            movie.getLanguage(),
                                            movie.getReleaseDate(),
                                            movie.getPosterUrl(),
                                            movie.getTrailerUrl(),
                                            movie.getRating(),
                                            movie.getCastList(),
                                            movie.getCrewList(),
                                            null,
                                            null,
                                            movie.getImageId()
                                    )
                            )
                            .toList();
                    MovieListResponseDTO movieListResponseDTOs = new MovieListResponseDTO(
                            movieResponseDTOs,
                            listMoviesResponse.getTotalCount(),
                            listMoviesResponse.getPage(),
                            listMoviesResponse.getSize(),
                            listMoviesResponse.getTotalPages(),
                            listMoviesResponse.getHasNext()
                    );

                    return Mono.just(ResponseEntity.ok(movieListResponseDTOs));
                });
    }

    @Operation(summary = "Get an image by ID", description = "Retrieves an image by its ID from the movie service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Image retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MovieImageResponseDTO.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized access"),
            @ApiResponse(responseCode = "404",
                    description = "Image not found")
    })
    @GetMapping("/images/{imageId}")
    public Mono<ResponseEntity<byte[]>> getImageById(@PathVariable Long imageId) {
        log.info("Received request to get image with ID: {}", imageId);
        return Mono.fromCallable(() -> imageGrpcService.getImageById(imageId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(movieImageResponseDTO -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(movieImageResponseDTO.contentType()))
                        .contentLength(movieImageResponseDTO.size())
                        .body(movieImageResponseDTO.data()));
    }

}