package com.microservices.gateway.services.gRPCServices;

import com.google.protobuf.ByteString;

import com.microservices.gateway.DTOS.movie.MovieRequestDTO;
import com.microservices.gateway.DTOS.movie.MovieListResponseDTO;
import com.microservices.gateway.DTOS.movie.MovieResponseDTO;
import com.microservices.gateway.DTOS.movie.MovieUpdateRequestDTO;
import com.microservices.movie.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieGrpcService {

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieServiceBlockingStub;


    public MovieResponseDTO create(MovieRequestDTO movieRequestDTO, FilePart movieImage) throws IOException {

        log.info("creating movie with title {}", movieRequestDTO.title());

        MovieImage image = null;

        if (movieImage != null) {
            log.info("Processing movie image with filename: {}", movieImage.filename());
            DataBuffer dataBuffer = DataBufferUtils.join(movieImage.content()).block();

            if (dataBuffer != null) {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                image = MovieImage.newBuilder()
                        .setData(ByteString.copyFrom(bytes))
                        .setContentType(movieImage.headers().getContentType().toString())
                        .setSize(bytes.length)
                        .setFileName(movieImage.filename())
                        .build();

                log.info("Content type stored in protobuf: {}", image.getContentType());
            }
        }else{
            image = MovieImage.newBuilder().build();
        }

        CreateMovieRequest createMovieRequest = CreateMovieRequest.newBuilder()
                .setTitle(movieRequestDTO.title())
                .setDescription(movieRequestDTO.description())
                .setDurationMinutes(movieRequestDTO.durationMinutes())
                .setGenre(movieRequestDTO.genre())
                .setLanguage(movieRequestDTO.language())
                .setReleaseDate(movieRequestDTO.releaseDate())
                .setPosterUrl(movieRequestDTO.posterUrl())
                .setTrailerUrl(movieRequestDTO.trailerUrl())
                .setRating(movieRequestDTO.rating())
                .addAllCast(movieRequestDTO.cast())
                .addAllCrew(movieRequestDTO.crew())
                .setImage(image)
                .build();

        MovieResponse movieResponse = movieServiceBlockingStub.createMovie(createMovieRequest);
        log.info("movie created with id {}", movieResponse.getMovieId());

        MovieResponseDTO movieResponseDTO = new MovieResponseDTO(
                movieResponse.getMovieId(),
                movieResponse.getTitle(),
                movieResponse.getDescription(),
                movieResponse.getDurationMinutes(),
                movieResponse.getGenre(),
                movieResponse.getLanguage(),
                movieResponse.getReleaseDate(),
                movieResponse.getPosterUrl(),
                movieResponse.getTrailerUrl(),
                movieResponse.getRating(),
                movieResponse.getCastList(),
                movieResponse.getCrewList(),
                null,
                null,
                movieResponse.getImageId()
        );

        return movieResponseDTO;
    }


    public MovieResponseDTO updateMovie(MovieUpdateRequestDTO movieRequestDTO, FilePart movieImage) {
        log.info("updating movie with id {}", movieRequestDTO.movieId());


        MovieImage image = null;

        if (movieImage != null) {
            log.info("Processing movie image with filename: {}", movieImage.filename());
            DataBuffer dataBuffer = DataBufferUtils.join(movieImage.content()).block();

            if (dataBuffer != null) {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                image = MovieImage.newBuilder()
                        .setData(ByteString.copyFrom(bytes))
                        .setContentType(movieImage.headers().getContentType().toString())
                        .setSize(bytes.length)
                        .setFileName(movieImage.filename())
                        .build();

                log.info("Content type stored in protobuf: {}", image.getContentType());
            }
        }else{
            image = MovieImage.newBuilder().build();
        }

       UpdateMovieRequest updateMovieRequest = UpdateMovieRequest.newBuilder()
                .setMovieId(movieRequestDTO.movieId())
                .setTitle(movieRequestDTO.title())
                .setDescription(movieRequestDTO.description())
                .setDurationMinutes(movieRequestDTO.durationMinutes())
                .setGenre(movieRequestDTO.genre())
                .setLanguage(movieRequestDTO.language())
                .setReleaseDate(movieRequestDTO.releaseDate())
                .setPosterUrl(movieRequestDTO.posterUrl())
                .setTrailerUrl(movieRequestDTO.trailerUrl())
                .setRating(movieRequestDTO.rating())
                .addAllCast(movieRequestDTO.cast())
                .addAllCrew(movieRequestDTO.crew())
                .setImage(image)
                .build();

        MovieResponse response = movieServiceBlockingStub.updateMovie(updateMovieRequest);
        log.info("movie updated with id {}", response.getMovieId());

        MovieResponseDTO movieResponseDTO = new MovieResponseDTO(
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
                null,
                null,
                response.getImageId()
        );
        return movieResponseDTO;
    }


    public MovieResponseDTO getMovie(String movieId) {
       GetMovieRequest getMovieRequest = GetMovieRequest.newBuilder()
                .setMovieId(movieId)
                .build();

        MovieResponse response = movieServiceBlockingStub.getMovie(getMovieRequest);
        log.info("movie retrieved with id {}", response.getMovieId());

        MovieResponseDTO movieResponseDTO = new MovieResponseDTO(
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
                null,
                null,
                response.getImageId()
        );
        return movieResponseDTO;
    }

    public MovieListResponseDTO searchMovies(String query, int limit) {
        SearchMoviesRequest searchMoviesRequest = SearchMoviesRequest.newBuilder()
                .setQuery(query == null ? "" : query)
                .setLimit(limit)
                .build();

        ListMoviesResponse response = movieServiceBlockingStub.searchMovies(searchMoviesRequest);
        log.info("movies found for search query '{}' with count {}", query, response.getMoviesCount());

        List<MovieResponseDTO> movieResponseDTOs = response.getMoviesList().stream()
                .map(movie -> new MovieResponseDTO(
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
                ))
                .collect(Collectors.toList());

        return new MovieListResponseDTO(
                movieResponseDTOs,
                response.getTotalCount(),
                response.getPage(),
                response.getSize(),
                response.getTotalPages(),
                response.getHasNext()
        );
    }

    public ListMoviesResponse listMovies(int page, int size, String genre, String language) {

        ListMoviesRequest listMoviesRequest = ListMoviesRequest.newBuilder()
                .setGenre(genre != null ? genre : "")
                .setLanguage(language != null ? language : "")
                .setPage(page)
                .setSize(size)
                .build();

        ListMoviesResponse response = movieServiceBlockingStub.listMovies(listMoviesRequest);
        log.info("movies retrieved with count {}", response.getMoviesCount());

        return response;

    }

    public boolean deleteMovieById(String movieId) {
        DeleteMovieRequest deleteMovieRequest = DeleteMovieRequest.newBuilder()
                .setMovieId(movieId)
                .build();

        DeleteMovieResponse response = movieServiceBlockingStub.deleteMovie(deleteMovieRequest);
        log.info("movie deleted with id {}", movieId);
        return response.getSuccess();
    }
}
