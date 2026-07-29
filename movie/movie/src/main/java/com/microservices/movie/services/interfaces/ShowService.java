package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Show;
import com.microservices.movie.models.enums.SeatStatus;
import com.microservices.movie.models.enums.SeatType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShowService {

    Show createShow(Show show);

    Show updateShow(UUID showId, Show show);

    Show getShow(UUID showId);

    List<Show> listShowsByMovie(UUID movieId);

    List<Show> listShowsByTheater(UUID theaterId);

    List<SeatAvailability> getAvailableSeats(UUID showId);


    List<ShowTimeAvailability> getAvailableShowTimesForDate(LocalDate date, UUID screenId, Integer movieDurationMinutes);

    record SeatAvailability(
            UUID seatId,
            String rowName,
            Integer seatNumber,
            SeatType seatType,
            Double price,
            SeatStatus status
    ) {
    }

    record ShowTimeAvailability(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
    }
}
