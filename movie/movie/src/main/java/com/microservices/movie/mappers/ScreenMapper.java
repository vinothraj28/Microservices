package com.microservices.movie.mappers;

import com.google.protobuf.Timestamp;
import com.microservices.movie.grpc.AddScreenRequest;
import com.microservices.movie.grpc.ScreenResponse;
import com.microservices.movie.grpc.SeatLayoutResponse;
import com.microservices.movie.models.entities.Screen;
import com.microservices.movie.models.entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ScreenMapper {

    Screen toScreen(AddScreenRequest request);

    @Mapping(target = "screenId", expression = "java(screen.getId().toString())")
    @Mapping(target = "theaterId", expression = "java(screen.getTheater().getId().toString())")
    @Mapping(target = "screenType", expression = "java(screen.getScreenType().name())")
    //@Mapping(target = "seatLayoutList", expression = "java(mapSeatLayout(screen.getSeats()))")
    @Mapping(target = "seatLayoutList", ignore = true)
    ScreenResponse toScreenResponse(Screen screen);

    default List<SeatLayoutResponse> mapSeatLayout(List<Seat> seats) {
        if (seats == null || seats.isEmpty()) {
            return Collections.emptyList();
        }

        // Group seats by rowName and seatType
        Map<String, Map<String, List<Seat>>> groupedSeats = seats.stream()
                .collect(Collectors.groupingBy(
                        Seat::getRowName,
                        Collectors.groupingBy(seat -> seat.getSeatType().name())
                ));

        List<SeatLayoutResponse> layoutResponses = new ArrayList<>();

        groupedSeats.forEach((rowName, seatTypeMap) -> {
            seatTypeMap.forEach((seatType, seatsInGroup) -> {
                // Sort seats by seat number
                seatsInGroup.sort(Comparator.comparing(Seat::getSeatNumber));

                // Create ranges for consecutive seats
                List<List<Seat>> ranges = new ArrayList<>();
                List<Seat> currentRange = new ArrayList<>();

                for (Seat seat : seatsInGroup) {
                    if (currentRange.isEmpty() ||
                        seat.getSeatNumber() == currentRange.get(currentRange.size() - 1).getSeatNumber() + 1) {
                        currentRange.add(seat);
                    } else {
                        ranges.add(new ArrayList<>(currentRange));
                        currentRange.clear();
                        currentRange.add(seat);
                    }
                }
                if (!currentRange.isEmpty()) {
                    ranges.add(currentRange);
                }

                // Convert each range to SeatLayoutResponse
                for (List<Seat> range : ranges) {
                    layoutResponses.add(SeatLayoutResponse.newBuilder()
                            .setRowName(rowName)
                            .setStartSeatNumber(range.get(0).getSeatNumber())
                            .setEndSeatNumber(range.get(range.size() - 1).getSeatNumber())
                            .setSeatType(seatType)
                            .setPriceMultiplier(range.get(0).getPriceMultiplier())
                            .build());
                }
            });
        });

        return layoutResponses;
    }

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