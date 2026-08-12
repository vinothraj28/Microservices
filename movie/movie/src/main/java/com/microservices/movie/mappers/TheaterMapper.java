package com.microservices.movie.mappers;

import com.google.protobuf.Timestamp;
import com.microservices.movie.grpc.CreateTheaterRequest;
import com.microservices.movie.grpc.TheaterResponse;
import com.microservices.movie.grpc.UpdateTheaterRequest;
import com.microservices.movie.models.entities.Theater;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface TheaterMapper {

    @Mapping(target = "id", source = "theaterId")
    public Theater toTheater(UpdateTheaterRequest theaterRequest);

    public Theater toTheater(CreateTheaterRequest theaterRequest);

    @Mapping(target = "theaterId", source = "id")
    public TheaterResponse toTheaterResponse(Theater theater);


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
