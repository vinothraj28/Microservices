package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Screen;
import com.microservices.movie.models.entities.Theater;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TheaterService {

    enum DeleteRequest{
        SUCCESS, NOT_FOUND, HAS_ACTIVE_SHOWS, CONFLICT
    }

    Theater createTheater(Theater theater);

    Theater updateTheater(UUID theaterId, Theater theater);

    Theater getTheater(UUID theaterId);

    Page<Theater> listTheaters(int page, int size, String city);

    Screen addScreen(UUID theaterId, Screen screen);

    Screen updateScreen(UUID theaterId, UUID screenId, Screen screen);

    Screen getScreen(UUID screenId);

    List<Screen> listScreensByTheater(UUID theaterId);

    DeleteRequest deleteTheater(UUID uuid);
}
