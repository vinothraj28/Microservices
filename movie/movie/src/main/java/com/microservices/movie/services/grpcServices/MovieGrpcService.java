package com.microservices.movie.services.grpcServices;

import com.microservices.movie.grpc.*;
import com.microservices.movie.mappers.MovieMapper;
import com.microservices.movie.models.entities.Image;
import com.microservices.movie.models.entities.Movie;
import com.microservices.movie.services.interfaces.MovieService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@GrpcService
@RequiredArgsConstructor
@Getter
@Setter
public class MovieGrpcService extends MovieServiceGrpc.MovieServiceImplBase {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Override
    public void createMovie(CreateMovieRequest request,
                            StreamObserver<MovieResponse> responseObserver) {
        log.info("Received gRPC request to create movie with title: {}", request.getTitle());

        Image image = null;

        try{

            if(request.hasImage()) {
                log.info("Image filename: {}", request.getImage().getFileName());
                log.info("Image content type: {}", request.getImage().getContentType());
                log.info("Image size: {}", request.getImage().getSize());
                log.info("Image data size: {}", request.getImage().getData().size());
                image = Image.builder()
                        .fileName(request.getImage().getFileName())
                        .contentType(request.getImage().getContentType())
                        .size((long) request.getImage().getSize())
                        .data(request.getImage().getData().toByteArray())
                        .build();
            }

            Movie movie = Movie.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .durationMinutes(request.getDurationMinutes())
                    .genre(request.getGenre())
                    .language(request.getLanguage())
                    .releaseDate((Date.valueOf(request.getReleaseDate()).toLocalDate()))
                    .posterUrl(request.getPosterUrl())
                    .trailerUrl(request.getTrailerUrl())
                    .rating(request.getRating())
                    .cast(request.getCastList())
                    .crew(request.getCrewList())
                    .image(image)
                    .build();

            Movie response = movieService.createMovie(movie);
            MovieResponse movieResponse = MovieResponse.newBuilder()
                    .setMovieId(response.getId().toString())
                    .setTitle(response.getTitle())
                    .setDescription(response.getDescription())
                    .setDurationMinutes(response.getDurationMinutes())
                    .setGenre(response.getGenre())
                    .setLanguage(response.getLanguage())
                    .setReleaseDate(response.getReleaseDate().toString())
                    .setPosterUrl(response.getPosterUrl())
                    .setTrailerUrl(response.getTrailerUrl())
                    .setRating(response.getRating())
                    .addAllCast(response.getCast())
                    .addAllCrew(response.getCrew())
                    .setImageId(response.getImage() != null ? String.valueOf(response.getImage().getId()) : "")
                    .build();
            responseObserver.onNext(movieResponse);
            responseObserver.onCompleted();
        }catch (IllegalArgumentException ex){
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid movie data")
                            .withCause(ex)
                            .asRuntimeException()
            );
        }catch (Exception e){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void updateMovie(UpdateMovieRequest request, StreamObserver<MovieResponse> responseObserver) {
        log.info("Received gRPC request to update movie with ID: {}", request.getMovieId());
        Image image = null;

        try {
            if (request.hasImage()) {
                log.info("Image filename: {}", request.getImage().getFileName());
                log.info("Image content type: {}", request.getImage().getContentType());
                log.info("Image size: {}", request.getImage().getSize());
                log.info("Image data size: {}", request.getImage().getData().size());
                image = Image.builder()
                        .fileName(request.getImage().getFileName())
                        .contentType(request.getImage().getContentType())
                        .size((long) request.getImage().getSize())
                        .data(request.getImage().getData().toByteArray())
                        .build();
            }

            Movie movie = Movie.builder()
                    .id(UUID.fromString(request.getMovieId()))
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .durationMinutes(request.getDurationMinutes())
                    .genre(request.getGenre())
                    .language(request.getLanguage())
                    .releaseDate((Date.valueOf(request.getReleaseDate()).toLocalDate()))
                    .posterUrl(request.getPosterUrl())
                    .trailerUrl(request.getTrailerUrl())
                    .rating(request.getRating())
                    .cast(new ArrayList<>(request.getCastList()))
                    .crew(new ArrayList<>(request.getCrewList()))
                    .image(image)
                    .build();

            Movie response = movieService.updateMovie(movie.getId(), movie);
            MovieResponse movieResponse = MovieResponse.newBuilder()
                    .setMovieId(response.getId().toString())
                    .setTitle(response.getTitle())
                    .setDescription(response.getDescription())
                    .setDurationMinutes(response.getDurationMinutes())
                    .setGenre(response.getGenre())
                    .setLanguage(response.getLanguage())
                    .setReleaseDate(response.getReleaseDate().toString())
                    .setPosterUrl(response.getPosterUrl())
                    .setTrailerUrl(response.getTrailerUrl())
                    .setRating(response.getRating())
                    .addAllCast(response.getCast())
                    .addAllCrew(response.getCrew())
                    .setImageId(response.getImage() != null ? String.valueOf(response.getImage().getId()) : "")
                    .build();
            responseObserver.onNext(movieResponse);
            responseObserver.onCompleted();
        }catch (IllegalArgumentException e) {
            log.error("Invalid argument", e);
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid argument")
                    .withCause(e)
                    .asRuntimeException()
            );
        }catch (Exception e) {
            log.error("Error updating movie", e);
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error")
                    .withCause(e)
                    .asRuntimeException()
            );
        }
    }

    public void getMovie(GetMovieRequest getMovieRequest, StreamObserver<MovieResponse> responseObserver) {
        try {
            log.info("Received gRPC request to get movie with ID: {}", getMovieRequest.getMovieId());

            Movie movie = movieService.getMovie(UUID.fromString(getMovieRequest.getMovieId()));
            MovieResponse movieResponse = MovieResponse.newBuilder()
                    .setMovieId(movie.getId().toString())
                    .setTitle(movie.getTitle())
                    .setDescription(movie.getDescription())
                    .setDurationMinutes(movie.getDurationMinutes())
                    .setGenre(movie.getGenre())
                    .setLanguage(movie.getLanguage())
                    .setReleaseDate(movie.getReleaseDate().toString())
                    .setPosterUrl(movie.getPosterUrl())
                    .setTrailerUrl(movie.getTrailerUrl())
                    .setRating(movie.getRating())
                    .addAllCast(movie.getCast())
                    .addAllCrew(movie.getCrew())
                    .setImageId(movie.getImage() != null ? String.valueOf(movie.getImage().getId()) : "")
                    .build();
            responseObserver.onNext(movieResponse);
            responseObserver.onCompleted();
        }catch (Exception e) {
            log.error("Error getting movie", e);
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error")
                    .withCause(e)
                    .asRuntimeException()
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void searchMovies(SearchMoviesRequest request, StreamObserver<ListMoviesResponse> responseObserver) {
        try {
            log.info("Received gRPC request to search movies with query '{}' and limit {}", request.getQuery(), request.getLimit());

            Page<Movie> moviePage = movieService.searchMovies(request.getQuery(), request.getLimit());
            ListMoviesResponse.Builder responseBuilder = ListMoviesResponse.newBuilder();
            moviePage.forEach(movie -> responseBuilder.addMovies(movieMapper.toMovieResponse(movie)));
            responseBuilder.setTotalCount((int) moviePage.getTotalElements());
            responseBuilder.setPage(moviePage.getNumber());
            responseBuilder.setSize(moviePage.getSize());
            responseBuilder.setTotalPages(moviePage.getTotalPages());
            responseBuilder.setHasNext(moviePage.hasNext());

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error searching movies", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

    @Override
        public void deleteMovie(DeleteMovieRequest request,
                               StreamObserver<DeleteMovieResponse> responseObserver) {
            try {
                MovieService.DeleteResult result = movieService.deleteMovie(
                    UUID.fromString(request.getMovieId())
                );

                switch (result) {
                    case SUCCESS -> {
                        responseObserver.onNext(DeleteMovieResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Movie deleted successfully")
                            .build());
                        responseObserver.onCompleted();
                    }
                    case NOT_FOUND -> responseObserver.onError(
                        Status.NOT_FOUND
                            .withDescription("Movie not found")
                            .asRuntimeException()
                    );
                    case HAS_ACTIVE_SHOWS -> responseObserver.onError(
                        Status.FAILED_PRECONDITION
                            .withDescription("Cannot delete movie with active shows")
                            .asRuntimeException()
                    );
                }
            }catch (IllegalArgumentException e) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Invalid movie ID")
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
    public void listMovies(ListMoviesRequest listMoviesRequest, StreamObserver<ListMoviesResponse> responseObserver) {

        try {
            log.info("Received gRPC request to list all movies");
            Page<Movie> moviePage = movieService.listMovies(listMoviesRequest.getPage(),
                    listMoviesRequest.getSize(), listMoviesRequest.getGenre(), listMoviesRequest.getLanguage());

            ListMoviesResponse response = ListMoviesResponse.newBuilder()
                    .addAllMovies(
                            moviePage.getContent()
                                    .stream()
                                    .map(movieMapper::toMovieResponse)
                                    .toList()
                    )
                    .setTotalCount((int) moviePage.getTotalElements())
                    .setPage(moviePage.getNumber())
                    .setSize(moviePage.getSize())
                    .setHasNext(moviePage.hasNext())
                    .setTotalPages(moviePage.getTotalPages())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error listing movies", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );

        }
    }

}
