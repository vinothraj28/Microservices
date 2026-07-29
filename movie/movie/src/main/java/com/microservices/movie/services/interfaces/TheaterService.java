package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Screen;
import com.microservices.movie.models.entities.Theater;

import java.util.List;
import java.util.UUID;

public interface TheaterService {

    Theater createTheater(Theater theater);

    Theater updateTheater(UUID theaterId, Theater theater);

    Theater getTheater(UUID theaterId);

    List<Theater> listTheaters();

    Screen addScreen(UUID theaterId, Screen screen);

    Screen updateScreen(UUID theaterId, UUID screenId, Screen screen);

    Screen getScreen(UUID screenId);

    List<Screen> listScreensByTheater(UUID theaterId);
}
