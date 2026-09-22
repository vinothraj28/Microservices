package com.microservices.movie.mappers;

import com.microservices.movie.grpc.MovieResponse;
import com.microservices.movie.models.entities.Movie;
import org.mapstruct.Mapper;

import com.google.protobuf.Timestamp;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface MovieMapper {

    @Mapping(target = "movieId", source = "id")
    @Mapping(
            target = "imageId",
            expression = "java(movie.getImage() != null ? String.valueOf(movie.getImage().getId()) : \"/\")"
    )
    public MovieResponse toMovieResponse(Movie movie);

}
