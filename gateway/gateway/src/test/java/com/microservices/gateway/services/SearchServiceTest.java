package com.microservices.gateway.services;

import com.microservices.gateway.DTOS.movie.MovieListResponseDTO;
import com.microservices.gateway.DTOS.movie.MovieResponseDTO;
import com.microservices.gateway.DTOS.search.SearchSuggestionDTO;
import com.microservices.gateway.DTOS.show.ShowListResponseDTO;
import com.microservices.gateway.DTOS.show.ShowResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterListResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterResponseDTO;
import com.microservices.gateway.services.gRPCServices.MovieGrpcService;
import com.microservices.gateway.services.gRPCServices.ShowGRPCService;
import com.microservices.gateway.services.gRPCServices.TheaterGRPCService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private MovieGrpcService movieGrpcService;

    @Mock
    private TheaterGRPCService theaterGRPCService;

    @Mock
    private ShowGRPCService showGRPCService;

    @InjectMocks
    private SearchService searchService;

    @Test
    void returnsEmptyListForBlankQuery() {
        List<SearchSuggestionDTO> suggestions = searchService.searchSuggestions("  ");

        assertEquals(List.of(), suggestions);
        verify(movieGrpcService, never()).searchMovies(anyString(), anyInt());
        verify(theaterGRPCService, never()).searchTheaters(anyString(), anyInt());
    }

    @Test
    void returnsMovieSuggestions() {
        when(movieGrpcService.searchMovies("spider", 5)).thenReturn(movieResults());
        when(theaterGRPCService.searchTheaters("spider", 5)).thenReturn(emptyTheaterResults());

        List<SearchSuggestionDTO> suggestions = searchService.searchSuggestions(" spider ");

        assertEquals(1, suggestions.size());
        assertEquals("MOVIE", suggestions.get(0).type());
        assertEquals("Spider-Man", suggestions.get(0).title());
    }

    @Test
    void returnsTheaterSuggestions() {
        when(movieGrpcService.searchMovies("spider", 5)).thenReturn(emptyMovieResults());
        when(theaterGRPCService.searchTheaters("spider", 5)).thenReturn(theaterResults());

        List<SearchSuggestionDTO> suggestions = searchService.searchSuggestions("spider");

        assertEquals(1, suggestions.size());
        assertEquals("THEATER", suggestions.get(0).type());
        assertEquals("Spider Cinema", suggestions.get(0).title());
    }

    @Test
    void combinesMovieAndTheaterSuggestions() {
        when(movieGrpcService.searchMovies("spider", 5)).thenReturn(movieResults());
        when(theaterGRPCService.searchTheaters("spider", 5)).thenReturn(theaterResults());

        List<SearchSuggestionDTO> suggestions = searchService.searchSuggestions("spider");

        assertEquals(2, suggestions.size());
    }

    @Test
    void returnsEmptyListWhenNoResults() {
        when(movieGrpcService.searchMovies("spider", 5)).thenReturn(emptyMovieResults());
        when(theaterGRPCService.searchTheaters("spider", 5)).thenReturn(emptyTheaterResults());

        List<SearchSuggestionDTO> suggestions = searchService.searchSuggestions("spider");

        assertEquals(List.of(), suggestions);
    }

    @Test
    void throwsWhenMovieSearchFailsAndTheaterReturnsNothing() {
        when(movieGrpcService.searchMovies("spider", 5)).thenThrow(new RuntimeException("movie failed"));
        when(theaterGRPCService.searchTheaters("spider", 5)).thenReturn(emptyTheaterResults());

        assertThrows(RuntimeException.class, () -> searchService.searchSuggestions("spider"));
    }

    @Test
    void throwsWhenTheaterSearchFailsAndMovieReturnsNothing() {
        when(movieGrpcService.searchMovies("spider", 5)).thenReturn(emptyMovieResults());
        when(theaterGRPCService.searchTheaters("spider", 5)).thenThrow(new RuntimeException("theater failed"));

        assertThrows(RuntimeException.class, () -> searchService.searchSuggestions("spider"));
    }

    @Test
    void delegatesShowSearchByMovieId() {
        ShowListResponseDTO response = ShowListResponseDTO.fromShowsAndTotalCount(List.of(), 0);
        when(showGRPCService.listShowsByMovie("movie-1", "2026-09-20", "Amsterdam")).thenReturn(response);

        ShowListResponseDTO result = searchService.searchShows("movie-1", null, "2026-09-20", "Amsterdam", null, null, null, 0, 20);

        assertEquals(response, result);
        verify(showGRPCService).listShowsByMovie("movie-1", "2026-09-20", "Amsterdam");
        verify(showGRPCService, never()).searchShows(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void delegatesShowSearchByTheaterId() {
        ShowListResponseDTO response = ShowListResponseDTO.fromShowsAndTotalCount(List.of(), 0);
        when(showGRPCService.listShowsByTheater("theater-1", "2026-09-20")).thenReturn(response);

        ShowListResponseDTO result = searchService.searchShows(null, "theater-1", "2026-09-20", null, null, null, null, 0, 20);

        assertEquals(response, result);
        verify(showGRPCService).listShowsByTheater("theater-1", "2026-09-20");
        verify(showGRPCService, never()).searchShows(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void delegatesCombinedShowSearchWhenMovieAndTheaterAreProvided() {
        ShowResponseDTO show = new ShowResponseDTO(
                "show-1",
                "movie-1",
                "screen-1",
                "theater-1",
                null,
                null,
                "2026-09-20T10:00:00",
                100.0,
                "MORNING",
                20,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        ShowListResponseDTO response = ShowListResponseDTO.from(List.of(show), 1, 1, false);
        when(showGRPCService.searchShows("movie-1", "theater-1", "2026-09-20", "Amsterdam", null, null, null, 0, 20)).thenReturn(response);

        ShowListResponseDTO result = searchService.searchShows("movie-1", "theater-1", "2026-09-20", "Amsterdam", null, null, null, 0, 20);

        assertEquals(response, result);
        verify(showGRPCService).searchShows("movie-1", "theater-1", "2026-09-20", "Amsterdam", null, null, null, 0, 20);
    }

    @Test
    void filtersShowsByShowTypeGenreAndLanguage() {
        MovieResponseDTO englishActionMovie = new MovieResponseDTO(
                "movie-1",
                "Spider-Man",
                "Marvel hero",
                120,
                "Action",
                "English",
                "2026-09-20",
                "",
                "",
                "PG-13",
                List.of("Actor"),
                List.of("Director"),
                null,
                null,
                ""
        );
        MovieResponseDTO tamilDramaMovie = new MovieResponseDTO(
                "movie-2",
                "Drama Movie",
                "Drama story",
                130,
                "Drama",
                "Tamil",
                "2026-09-20",
                "",
                "",
                "U",
                List.of("Actor"),
                List.of("Director"),
                null,
                null,
                ""
        );
        ShowResponseDTO matchingShow = new ShowResponseDTO(
                "show-1",
                "movie-1",
                "screen-1",
                "theater-1",
                englishActionMovie,
                null,
                "2026-09-20T10:00:00",
                100.0,
                "MORNING",
                20,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        ShowResponseDTO nonMatchingShow = new ShowResponseDTO(
                "show-2",
                "movie-2",
                "screen-1",
                "theater-1",
                tamilDramaMovie,
                null,
                "2026-09-20T13:00:00",
                100.0,
                "EVENING",
                20,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        ShowListResponseDTO response = ShowListResponseDTO.from(List.of(matchingShow), 1, 1, false);
        when(showGRPCService.searchShows("movie-1", null, "2026-09-20", "Amsterdam", "morning", "action", "english", 0, 20))
                .thenReturn(response);

        ShowListResponseDTO result = searchService.searchShows(
                "movie-1",
                null,
                "2026-09-20",
                "Amsterdam",
                "morning",
                "action",
                "english",
                0,
                20
        );

        assertEquals(1, result.totalCount());
        assertEquals(1, result.shows().size());
        assertEquals("show-1", result.shows().get(0).showId());
        verify(showGRPCService).searchShows("movie-1", null, "2026-09-20", "Amsterdam", "morning", "action", "english", 0, 20);
    }

    private MovieListResponseDTO movieResults() {
        MovieResponseDTO movie = new MovieResponseDTO(
                "movie-1",
                "Spider-Man",
                "Marvel hero",
                120,
                "Action",
                "English",
                "2026-09-20",
                "",
                "",
                "PG-13",
                List.of("Actor"),
                List.of("Director"),
                null,
                null,
                ""
        );
        return new MovieListResponseDTO(List.of(movie), 1, 0, 5, 1, false);
    }

    private MovieListResponseDTO emptyMovieResults() {
        return new MovieListResponseDTO(List.of(), 0, 0, 5, 0, false);
    }

    private TheaterListResponseDTO theaterResults() {
        TheaterResponseDTO theater = new TheaterResponseDTO(
                "theater-1",
                "Spider Cinema",
                "Main Street",
                "Amsterdam",
                "NH",
                "1000AA",
                0.0,
                0.0,
                "1234567890",
                "info@example.com",
                List.of("Parking"),
                null,
                null
        );
        return new TheaterListResponseDTO(List.of(theater), 1, 0, 5, 1, false);
    }

    private TheaterListResponseDTO emptyTheaterResults() {
        return new TheaterListResponseDTO(List.of(), 0, 0, 5, 0, false);
    }
}
