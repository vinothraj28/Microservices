package com.microservices.movie.services.impl;

import com.microservices.movie.exceptions.InvalidRequestException;
import com.microservices.movie.exceptions.ResourceAlreadyExistsException;
import com.microservices.movie.exceptions.ResourceNotFoundException;
import com.microservices.movie.models.entities.*;
import com.microservices.movie.models.enums.BookingStatus;
import com.microservices.movie.models.enums.SeatStatus;
import com.microservices.movie.models.enums.ShowType;
import com.microservices.movie.repositories.*;
import com.microservices.movie.services.interfaces.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final SeatLockRepository seatLockRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private static final double DEFAULT_INTERVAL_CLEANING_TIME = 20.0;// in minutes
    private static final int DEFAULT_SHOW_START_TIME = 9;// in hours
    private static final int DEFAULT_SHOW_END_TIME = 23;// in hours

    @Override
    @Transactional
    public Show createShow(Show show) {
        UUID movieId = requireEntityId(show.getMovie(), "movie");
        UUID screenId = requireEntityId(show.getScreen(), "screen");
        UUID requestedTheaterId = show.getTheaterId();
        log.info("Creating show for movie {} on screen {} requested theater {}", movieId, screenId, requestedTheaterId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", movieId.toString()));
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId.toString()));

        if (screen.getTheater() == null) {
            throw new InvalidRequestException("Screen " + screenId + " is not assigned to a theater");
        }
        // Theater is always derived from the screen (single source of truth) rather than trusted from client input.
        if (requestedTheaterId != null && !requestedTheaterId.equals(screen.getTheater().getId())) {
            throw new InvalidRequestException("Provided theater does not match the screen's theater");
        }

        validateSchedule(screenId, show.getShowDateTime(), movie.getDurationMinutes(), null);

        Show showToPersist = Show.builder()
                .movie(movie)
                .screen(screen)
                .showDateTime(show.getShowDateTime())
                .basePrice(show.getBasePrice())
                .showType(show.getShowType())
                .build();
        // theaterId is populated automatically by Show's @PrePersist from screen.getTheater()
        return showRepository.save(showToPersist);
    }

    @Override
    @Transactional
    public Show updateShow(UUID showId, Show show) {
        log.info("Updating show with id: {}", showId);
        Show existingShow = getShow(showId);

        UUID movieId = requireEntityId(show.getMovie(), "movie");
        UUID screenId = requireEntityId(show.getScreen(), "screen");

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", movieId.toString()));
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId.toString()));

        LocalDateTime showDateTime = show.getShowDateTime() == null ? existingShow.getShowDateTime() : show.getShowDateTime();
        validateSchedule(screenId, showDateTime, movie.getDurationMinutes(), showId);

        existingShow.setMovie(movie);
        existingShow.setScreen(screen);
        existingShow.setShowDateTime(showDateTime);
        existingShow.setBasePrice(show.getBasePrice() == null ? existingShow.getBasePrice() : show.getBasePrice());
        existingShow.setShowType(show.getShowType() == null ? existingShow.getShowType() : show.getShowType());
        return showRepository.save(existingShow);
    }

    @Override
    @Transactional(readOnly = true)
    public Show getShow(UUID showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", showId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Show> listShowsByMovie(UUID movieId) {
        log.debug("Listing shows for movie {}", movieId);
        return showRepository.findByMovie_IdOrderByShowDateTimeAsc(movieId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Show> listShowsByTheater(UUID theaterId) {
        log.debug("Listing shows for theater {}", theaterId);
        return showRepository.findByScreen_Theater_IdOrderByShowDateTimeAsc(theaterId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatAvailability> getAvailableSeats(UUID showId) {
        Show show = getShow(showId);
        List<Seat> seats = seatRepository.findByScreenIdOrderByRowNameAscSeatNumberAsc(show.getScreen().getId());
        Set<UUID> bookedSeatIds = getBookedSeatIds(showId);
        Set<UUID> lockedSeatIds = getLockedSeatIds(showId);
        return seats.stream()
                .map(seat -> new SeatAvailability(
                        seat.getId(),
                        seat.getRowName(),
                        seat.getSeatNumber(),
                        seat.getSeatType(),
                        show.getBasePrice() * defaultMultiplier(seat),
                        resolveSeatStatus(seat.getId(), bookedSeatIds, lockedSeatIds)
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Show> listShows(int page, int size) {
        if( size == 0){
           size = 10;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("showDateTime").ascending());
        return showRepository.findByShowDateTimeGreaterThanEqual(LocalDate.now().atStartOfDay(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Show> searchShows(
            UUID movieId,
            UUID theaterId,
            LocalDate date,
            String city,
            String showType,
            String genre,
            String language,
            int page,
            int size
    ) {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = size > 0 ? size : 10;
        Pageable pageable = PageRequest.of(normalizedPage, normalizedSize, Sort.by("showDateTime").ascending());

        LocalDateTime startDateTime = date == null ? null : date.atStartOfDay();
        LocalDateTime endDateTime = date == null ? null : date.plusDays(1).atStartOfDay();

        ShowType parsedShowType = null;
        if (showType != null && !showType.isBlank()) {
            try {
                parsedShowType = ShowType.valueOf(showType.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new InvalidRequestException("Invalid showType. Allowed values: MORNING, MATINEE, EVENING, NIGHT");
            }
        }

        String normalizedCity = normalizeOptional(city);
        String normalizedGenre = normalizeOptional(genre);
        String normalizedLanguage = normalizeOptional(language);

        Specification<Show> specification = buildShowSearchSpecification(
                movieId,
                theaterId,
                startDateTime,
                endDateTime,
                normalizedCity,
                parsedShowType,
                normalizedGenre,
                normalizedLanguage
        );

        return showRepository.findAll(specification, pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ShowTimeAvailability> getAvailableShowTimesForDate(LocalDate date, UUID theaterId, UUID screenId, Integer movieDurationMinutes){

        if (date == null) {
            throw new InvalidRequestException("Date must be provided");
        }
        if (theaterId == null) {
            throw new InvalidRequestException("Theater Id must be provided");
        }
        if (screenId == null) {
            throw new InvalidRequestException("Screen Id must be provided");
        }
        if(movieDurationMinutes == null || movieDurationMinutes <= 0){
            throw new InvalidRequestException("Movie duration must be provided and greater than zero");
        }
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId.toString()));

        if (screen.getTheater() == null || !theaterId.equals(screen.getTheater().getId())) {
            throw new InvalidRequestException("Screen does not belong to the provided theater");
        }

        List<ShowTimeAvailability> availableShowTimes = new ArrayList<>();

        Duration requiredBufferDuration = Duration.ofMinutes((long) DEFAULT_INTERVAL_CLEANING_TIME);
        Duration movieDuration = Duration.ofMinutes(movieDurationMinutes).plus(requiredBufferDuration);

        LocalDateTime cursor = LocalDateTime.of(date, LocalTime.of(DEFAULT_SHOW_START_TIME, 0));

        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);

        List<Show> scheduledShows = showRepository.findByScreen_IdAndShowDateTimeBetween(screenId, dayStart, dayEnd);
        for (Show show : scheduledShows.stream().sorted(Comparator.comparing(Show::getShowDateTime)).toList()) {
            LocalDateTime showStart = show.getShowDateTime();
            LocalDateTime showEnd = showStart
                    .plusMinutes(show.getMovie().getDurationMinutes())
                    .plus(requiredBufferDuration);

            while(cursor.plus(movieDuration).isBefore(showStart) || cursor.plus(movieDuration).isEqual(showStart)) {
                availableShowTimes.add(new ShowTimeAvailability(cursor, cursor.plus(movieDuration)));
                cursor = cursor.plus(movieDuration);
            }

            if(cursor.isBefore(showEnd)) {
                cursor = showEnd;
            }
        }
        while(cursor.plus(movieDuration).isBefore(dayEnd) || cursor.plus(movieDuration).isEqual(dayEnd)) {
            availableShowTimes.add(new ShowTimeAvailability(cursor, cursor.plus(movieDuration)));
            cursor = cursor.plus(movieDuration);
        }
        return availableShowTimes;
    }

    private UUID requireEntityId(Object entity, String entityName) {
        if (entity instanceof Movie movie && movie.getId() != null) {
            return movie.getId();
        }
        if (entity instanceof Screen screen && screen.getId() != null) {
            return screen.getId();
        }
        if (entity instanceof Theater theater && theater.getId() != null) {
            return theater.getId();
        }
        throw new InvalidRequestException("Show " + entityName + " id is required");
    }

    private void validateSchedule(UUID screenId, LocalDateTime showStartTime, Integer durationMinutes, UUID currentShowId) {
        if (showStartTime == null) {
            throw new InvalidRequestException("Show date and time is required");
        }
        if (durationMinutes == null || durationMinutes <= 0) {
            throw new InvalidRequestException("Movie duration must be greater than zero");
        }

        LocalDateTime proposedShowEndTime = showStartTime.plusMinutes(durationMinutes);
        for (Show existingShow : showRepository.findByScreen_Id(screenId)) {
            if (currentShowId != null && existingShow.getId().equals(currentShowId)) {
                continue;
            }
            LocalDateTime existingStart = existingShow.getShowDateTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existingShow.getMovie().getDurationMinutes());
            boolean overlaps = showStartTime.isBefore(existingEnd) && proposedShowEndTime.isAfter(existingStart);
            if (overlaps) {
                throw new ResourceAlreadyExistsException(
                        String.format("Show timing overlaps with existing show %s on screen %s", existingShow.getId(), screenId)
                );
            }
        }
    }

    private Set<UUID> getBookedSeatIds(UUID showId) {
        Set<UUID> bookedSeatIds = new HashSet<>();
        List<Booking> confirmedBookings = bookingRepository.findByShow_IdAndStatus(showId, BookingStatus.CONFIRMED);
        for (Booking booking : confirmedBookings) {
            if (booking.getLockId() == null) {
                continue;
            }
            seatLockRepository.findById(booking.getLockId())
                    .ifPresent(lock -> bookedSeatIds.addAll(lock.getSeatIds()));
        }
        return bookedSeatIds;
    }

    private Set<UUID> getLockedSeatIds(UUID showId) {
        LocalDateTime now = LocalDateTime.now();
        Set<UUID> lockedSeatIds = new HashSet<>();
        List<SeatLock> activeLocks = seatLockRepository.findActiveLocksForShow(showId, now);
        activeLocks.forEach(lock -> lockedSeatIds.addAll(lock.getSeatIds()));
        return lockedSeatIds;
    }

    private SeatStatus resolveSeatStatus(UUID seatId, Set<UUID> bookedSeatIds, Set<UUID> lockedSeatIds) {
        if (bookedSeatIds.contains(seatId)) {
            return SeatStatus.BOOKED;
        }
        if (lockedSeatIds.contains(seatId)) {
            return SeatStatus.LOCKED;
        }
        return SeatStatus.AVAILABLE;
    }

    private double defaultMultiplier(Seat seat) {
        return seat.getPriceMultiplier() == null ? 1.0D : seat.getPriceMultiplier();
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private Specification<Show> buildShowSearchSpecification(
            UUID movieId,
            UUID theaterId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String city,
            ShowType showType,
            String genre,
            String language
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (movieId != null) {
                predicates.add(cb.equal(root.get("movie").get("id"), movieId));
            }
            if (theaterId != null) {
                predicates.add(cb.equal(root.get("theaterId"), theaterId));
            }
            if (startDateTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("showDateTime"), startDateTime));
            }
            if (endDateTime != null) {
                predicates.add(cb.lessThan(root.get("showDateTime"), endDateTime));
            }
            if (showType != null) {
                predicates.add(cb.equal(root.get("showType"), showType));
            }
            if (city != null) {
                Join<Show, Screen> screenJoin = root.join("screen", JoinType.INNER);
                Join<Screen, Theater> theaterJoin = screenJoin.join("theater", JoinType.INNER);
                predicates.add(cb.equal(cb.lower(theaterJoin.get("city")), city.toLowerCase(Locale.ROOT)));
            }
            if (genre != null || language != null) {
                Join<Show, Movie> movieJoin = root.join("movie", JoinType.INNER);
                if (genre != null) {
                    predicates.add(cb.equal(cb.lower(movieJoin.get("genre")), genre.toLowerCase(Locale.ROOT)));
                }
                if (language != null) {
                    predicates.add(cb.equal(cb.lower(movieJoin.get("language")), language.toLowerCase(Locale.ROOT)));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
