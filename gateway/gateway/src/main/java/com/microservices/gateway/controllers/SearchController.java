package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.search.SearchSuggestionDTO;
import com.microservices.gateway.DTOS.show.ShowListResponseDTO;
import com.microservices.gateway.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "Search and discovery endpoints")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Get search suggestions", description = "Returns lightweight movie and theater suggestions for autocomplete")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Suggestions retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SearchSuggestionDTO.class)))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid search query")
    })
    @GetMapping("/suggestions")
    public Mono<ResponseEntity<List<SearchSuggestionDTO>>> suggestions(@RequestParam(required = false) String q) {
        log.info("REST: Received search suggestions request for query '{}'", q);

        return Mono.fromCallable(() -> searchService.searchSuggestions(q))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("REST: Error getting search suggestions for query '{}'", q, error));
    }

    @Operation(summary = "Search shows", description = "Searches shows by movie, theater, date, and city")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shows retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ShowListResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping("/shows")
    public Mono<ResponseEntity<ShowListResponseDTO>> shows(
            @RequestParam(required = false) String movieId,
            @RequestParam(required = false) String theaterId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String showType,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("REST: Received show search request movieId={}, theaterId={}, date={}, city={}, showType={}, genre={}, language={}, page={}, size={}",
                movieId, theaterId, date, city, showType, genre, language, page, size);

        return Mono.fromCallable(() -> searchService.searchShows(movieId, theaterId, date, city, showType, genre, language, page, size))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("REST: Error searching shows movieId={}, theaterId={}, date={}, city={}, showType={}, genre={}, language={}",
                        movieId, theaterId, date, city, showType, genre, language, error));
    }
}
