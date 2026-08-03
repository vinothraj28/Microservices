package com.microservices.movie.mappers;

import com.microservices.movie.grpc.MovieResponse;
import com.microservices.movie.models.entities.Movie;
import org.mapstruct.Mapper;

import com.google.protobuf.Timestamp;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "movieId", source = "id")
    @Mapping(
            target = "imageId",
            expression = "java(movie.getImage() != null ? String.valueOf(movie.getImage().getId()) : \"/\")"
    )
    public MovieResponse toMovieResponse(Movie movie);

    default Instant toMap(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    default Timestamp toMap(Instant instant) {
        if (instant == null) {
            return null;
        }
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
