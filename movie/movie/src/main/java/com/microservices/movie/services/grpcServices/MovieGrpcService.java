package com.microservices.movie.services.grpcServices;

import com.microservices.movie.grpc.*;
import com.microservices.movie.mappers.MovieMapper;
import com.microservices.movie.models.entities.Image;
import com.microservices.movie.models.entities.Movie;
import com.microservices.movie.services.interfaces.MovieService;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;


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

        if(request.hasImage()) {
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
    }

    @Override
    public void updateMovie(UpdateMovieRequest request, StreamObserver<MovieResponse> responseObserver) {
        log.info("Received gRPC request to update movie with ID: {}", request.getMovieId());
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
    }

    public void getMovie(GetMovieRequest getMovieRequest, StreamObserver<MovieResponse> responseObserver) {

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
    }

    @Override
    public void deleteMovie(DeleteMovieRequest deleteMovieRequest, StreamObserver<DeleteMovieResponse> responseObserver) {
        log.info("Received gRPC request to delete movie with ID: {}", deleteMovieRequest.getMovieId());
        movieService.deleteMovie(UUID.fromString(deleteMovieRequest.getMovieId()));
        DeleteMovieResponse response = DeleteMovieResponse.newBuilder()
                .setMessage("Movie deleted successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listMovies(ListMoviesRequest listMoviesRequest, StreamObserver<ListMoviesResponse> responseObserver) {

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
    }

}
