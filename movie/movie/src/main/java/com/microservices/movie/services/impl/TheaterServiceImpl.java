package com.microservices.movie.services.impl;

import com.microservices.movie.exceptions.InvalidRequestException;
import com.microservices.movie.exceptions.ResourceAlreadyExistsException;
import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.models.entities.Screen;
import com.microservices.movie.models.entities.Seat;
import com.microservices.movie.models.entities.Theater;
import com.microservices.movie.models.enums.SeatType;
import com.microservices.movie.repositories.ScreenRepository;
import com.microservices.movie.repositories.SeatRepository;
import com.microservices.movie.repositories.ShowRepository;
import com.microservices.movie.repositories.TheaterRepository;
import com.microservices.movie.services.interfaces.TheaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private static final int DEFAULT_SEATS_PER_ROW = 10;

    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    @Override
    @Transactional
    public Theater createTheater(Theater theater) {
        log.info("Creating theater {} in city {}", theater.getName(), theater.getCity());
        if (theaterRepository.existsByNameAndCity(theater.getName(), theater.getCity())) {
            throw new ResourceAlreadyExistsException(
                    String.format("Theater '%s' already exists in city '%s'", theater.getName(), theater.getCity())
            );
        }
        return theaterRepository.save(theater);
    }

    @Override
    @Transactional
    public Theater updateTheater(UUID theaterId, Theater theater) {
        log.info("Updating theater with id: {}", theaterId);
        Theater existingTheater = getTheater(theaterId);
        boolean changingIdentity = !existingTheater.getName().equals(theater.getName())
                || !existingTheater.getCity().equals(theater.getCity());
        if (changingIdentity && theaterRepository.existsByNameAndCity(theater.getName(), theater.getCity())) {
            throw new ResourceAlreadyExistsException(
                    String.format("Theater '%s' already exists in city '%s'", theater.getName(), theater.getCity())
            );
        }
        existingTheater.setName(theater.getName());
        existingTheater.setAddress(theater.getAddress());
        existingTheater.setCity(theater.getCity());
        existingTheater.setState(theater.getState());
        existingTheater.setPincode(theater.getPincode());
        existingTheater.setLatitude(theater.getLatitude());
        existingTheater.setLongitude(theater.getLongitude());
        existingTheater.setPhone(theater.getPhone());
        existingTheater.setEmail(theater.getEmail());
        existingTheater.setAmenities(theater.getAmenities() == null ?
                new ArrayList<>() : new ArrayList<>(theater.getAmenities()));
        return theaterRepository.save(existingTheater);
    }

    @Override
    @Transactional(readOnly = true)
    public Theater getTheater(UUID theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", theaterId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Theater> listTheaters(int page, int size, String city) {
        log.debug("Listing all theaters");
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        if (city != null && !city.isEmpty()) {
            return theaterRepository.findByCity(city, pageable);
        }
        return theaterRepository.findAll(pageable);
    }



    @Override
    @Transactional
    public Screen addScreen(UUID theaterId, Screen screen) {
        log.info("Adding screen {} to theater {}", screen.getScreenNumber(), theaterId);
        Theater theater = getTheater(theaterId);
        if (screenRepository.existsByTheaterIdAndScreenNumber(theaterId, screen.getScreenNumber())) {
            throw new ResourceAlreadyExistsException(
                    String.format("Screen %d already exists for theater %s", screen.getScreenNumber(), theaterId)
            );
        }

        Screen screenToPersist = Screen.builder()
                .theater(theater)
                .screenName(screen.getScreenName())
                .screenNumber(screen.getScreenNumber())
                .screenType(screen.getScreenType())
                .totalSeats(screen.getTotalSeats()==null?0:screen.getTotalSeats())
                .build();

        Screen savedScreen = screenRepository.save(screenToPersist);
        List<Seat> seats = buildSeatLayout(savedScreen, screen);
        savedScreen.setSeats(seats);
        savedScreen.setTotalSeats(seats.size());
        Screen persistedScreen = screenRepository.save(savedScreen);
        seatRepository.saveAll(seats);
        return persistedScreen;
    }

    @Override
    @Transactional
    public Screen updateScreen(UUID theaterId, UUID screenId, Screen screen) {
        log.info("Updating screen {} for theater {}", screenId, theaterId);
        Theater theater = getTheater(theaterId);
        Screen existingScreen = getScreen(screenId);

        if (!existingScreen.getTheater().getId().equals(theater.getId())) {
            throw new ResourceNotFoundException("Screen", screenId.toString());
        }

        boolean changingScreenNumber = !existingScreen.getScreenNumber().equals(screen.getScreenNumber());
        if (changingScreenNumber
                && screenRepository.existsByTheaterIdAndScreenNumber(theaterId, screen.getScreenNumber())) {
            throw new ResourceAlreadyExistsException(
                    String.format("Screen %d already exists for theater %s", screen.getScreenNumber(), theaterId)
            );
        }

        existingScreen.setScreenName(screen.getScreenName());
        existingScreen.setScreenNumber(screen.getScreenNumber());
        existingScreen.setScreenType(screen.getScreenType());

        boolean rebuildLayout = shouldRebuildLayout(existingScreen, screen);
        if (rebuildLayout) {
            if (!showRepository.findByScreen_Id(screenId).isEmpty()) {
                throw new InvalidRequestException("Cannot change screen layout after shows have been scheduled");
            }
            List<Seat> newLayout = buildSeatLayout(existingScreen, screen);
            existingScreen.getSeats().clear();
            newLayout.forEach(existingScreen::addSeat);
            existingScreen.setTotalSeats(newLayout.size());
        } else if (screen.getTotalSeats() != null) {
            existingScreen.setTotalSeats(screen.getTotalSeats());
        }

        return screenRepository.save(existingScreen);
    }

    @Override
    @Transactional
    public TheaterService.DeleteRequest deleteTheater(UUID theaterId) {
        log.info("Deleting theater {}", theaterId);
        Theater theater = getTheater(theaterId);

        if(theater==null) return TheaterService.DeleteRequest.NOT_FOUND;

        if (!showRepository.findByTheaterId(theaterId).isEmpty()) {
            return TheaterService.DeleteRequest.HAS_ACTIVE_SHOWS;
        }

        theaterRepository.delete(theater);
        return TheaterService.DeleteRequest.SUCCESS;
    }


    @Override
    @Transactional(readOnly = true)
    public Screen getScreen(UUID screenId) {
        return screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getSeatLayoutByScreenId(UUID screenId) {
        Screen screen = getScreen(screenId);
        return screen.getSeats();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Screen> listScreensByTheater(UUID theaterId) {
        getTheater(theaterId);
        return screenRepository.findByTheaterId(theaterId);
    }

    private boolean shouldRebuildLayout(Screen existingScreen, Screen updatedScreen) {
        boolean seatsProvided = updatedScreen.getSeats() != null && !updatedScreen.getSeats().isEmpty();
        boolean totalSeatsChanged = updatedScreen.getTotalSeats() != null
                && !updatedScreen.getTotalSeats().equals(existingScreen.getTotalSeats());
        return seatsProvided || totalSeatsChanged;
    }

    private List<Seat> buildSeatLayout(Screen targetScreen, Screen sourceScreen) {
        List<Seat> sourceSeats = sourceScreen.getSeats() == null ? List.of() : sourceScreen.getSeats();
        if (!sourceSeats.isEmpty()) {
            log.debug("Using provided seat layout for screen {}", targetScreen.getScreenNumber());
            return sourceSeats.stream()
                    .sorted(Comparator.comparing(Seat::getRowName).thenComparing(Seat::getSeatNumber))
                    .map(seat -> Seat.builder()
                            .screen(targetScreen)
                            .rowName(seat.getRowName())
                            .seatNumber(seat.getSeatNumber())
                            .seatType(seat.getSeatType() == null ? SeatType.REGULAR : seat.getSeatType())
                            .priceMultiplier(seat.getPriceMultiplier() == null ? 1.0D : seat.getPriceMultiplier())
                            .build())
                    .toList();
        }

        int totalSeats = sourceScreen.getTotalSeats() == null ? 0 : sourceScreen.getTotalSeats();
        if (totalSeats <= 0) {
            throw new InvalidRequestException("Screen totalSeats must be greater than zero");
        }

        log.debug("Generating default seat layout with {} seats for screen {}", totalSeats, targetScreen.getScreenNumber());
        List<Seat> generatedSeats = new ArrayList<>();
        for (int index = 0; index < totalSeats; index++) {
            generatedSeats.add(Seat.builder()
                    .screen(targetScreen)
                    .rowName(getRowName(index / DEFAULT_SEATS_PER_ROW))
                    .seatNumber((index % DEFAULT_SEATS_PER_ROW) + 1)
                    .seatType(SeatType.REGULAR)
                    .priceMultiplier(1.0D)
                    .build());
        }
        return generatedSeats;
    }

    private String getRowName(int rowIndex) {
        StringBuilder rowName = new StringBuilder();
        int current = rowIndex;
        do {
            rowName.insert(0, (char) ('A' + (current % 26)));
            current = (current / 26) - 1;
        } while (current >= 0);
        return rowName.toString();
    }
}
