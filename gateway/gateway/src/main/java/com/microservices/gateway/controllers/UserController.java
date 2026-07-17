package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.LoginRequestDTO;
import com.microservices.gateway.DTOS.register.RegisterRequestDTO;
import com.microservices.gateway.DTOS.register.RegisterResponseDTO;
import com.microservices.gateway.services.gRPCServices.UserGRPCService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserGRPCService userGRPCService;

    public UserController(UserGRPCService userGRPCService) {
        this.userGRPCService = userGRPCService;
    }

//    @PostMapping("/login")
//    public Mono<ResponseEntity<String>> login(
//            @RequestBody LoginRequestDTO request
//    ) {
//        log.info("Fetching login via gRPC for the user {}", request.username());
//        return Mono.fromCallable( () -> userGRPCService.login(request).getToken()
//        ).subscribeOn(Schedulers.boundedElastic())
//                .map(ResponseEntity::ok);
//    }



    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        log.info("Registering user via gRPC service {} ", registerRequestDTO.userName());
        return Mono.fromCallable( () -> userGRPCService.register(registerRequestDTO)
                ).subscribeOn(Schedulers.boundedElastic())
                .map(response -> ResponseEntity.created(URI.create("/users/"+response.userId()))
                        .body(response));
    }
}
