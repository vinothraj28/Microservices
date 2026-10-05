package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.screen.AddScreenRequestDTO;
import com.microservices.gateway.DTOS.screen.ScreenResponseDTO;
import com.microservices.gateway.DTOS.screen.SeatLayoutResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterListResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterRequestDTO;
import com.microservices.gateway.DTOS.theater.TheaterResponseDTO;
import com.microservices.gateway.services.gRPCServices.TheaterGRPCService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
public class TheaterController {

    private final TheaterGRPCService theaterGRPCService;

    public TheaterController(TheaterGRPCService theaterGRPCService) {
        this.theaterGRPCService = theaterGRPCService;
    }

    @PostMapping("")
    public Mono<ResponseEntity<TheaterResponseDTO>> createTheater(@Valid @RequestBody TheaterRequestDTO theaterRequestDTO) {
        return Mono.fromCallable(() -> theaterGRPCService.CreateTheater(theaterRequestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("")
    public Mono<ResponseEntity<TheaterListResponseDTO>> getAllTheater(@RequestParam int size, @RequestParam int page
                                            ,@RequestParam(required = false) String city) {
        return Mono.fromCallable(() -> theaterGRPCService.getAllTheaters(page, size, city))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{theaterId}")
    public Mono<ResponseEntity<TheaterResponseDTO>> getTheaterById(@PathVariable String theaterId) {
        return Mono.fromCallable(() -> theaterGRPCService.getTheaterById(theaterId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{theaterId}")
    public Mono<ResponseEntity<TheaterResponseDTO>> updateTheater(@PathVariable String theaterId, @Valid @RequestBody TheaterRequestDTO theaterRequestDTO) {
        return Mono.fromCallable(() -> theaterGRPCService.updateTheater(theaterId, theaterRequestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{theaterId}")
    public Mono<ResponseEntity<Boolean>> deleteTheater(@PathVariable String theaterId) {
        return Mono.fromCallable(() -> theaterGRPCService.deleteTheaterById(theaterId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    // Screen Management Endpoints

    @PostMapping("/screens")
    public Mono<ResponseEntity<ScreenResponseDTO>> addScreen(
            @Valid @RequestBody AddScreenRequestDTO addScreenRequestDTO) {
        return Mono.fromCallable(() -> theaterGRPCService.addScreen(addScreenRequestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/screens/{screenId}")
    public Mono<ResponseEntity<ScreenResponseDTO>> getScreen(@PathVariable String screenId) {
        return Mono.fromCallable(() -> theaterGRPCService.getScreen(screenId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{theaterId}/screens/{screenId}")
    public Mono<ResponseEntity<ScreenResponseDTO>> updateScreen(@PathVariable String theaterId, @PathVariable String screenId, @Valid @RequestBody AddScreenRequestDTO updateScreenRequestDTO) {
        return Mono.fromCallable(() -> theaterGRPCService.updateScreen(theaterId, screenId, updateScreenRequestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{theaterId}/screens")
    public Mono<ResponseEntity<List<ScreenResponseDTO>>> listScreensByTheater(@PathVariable String theaterId) {
        return Mono.fromCallable(() -> theaterGRPCService.listScreensByTheater(theaterId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/screens/{screenId}/seat-layout")
    public Mono<ResponseEntity<List<SeatLayoutResponseDTO>>> getSeatLayout(@PathVariable String screenId){
        return Mono.fromCallable(() -> theaterGRPCService.getSeatLayout(screenId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }
}