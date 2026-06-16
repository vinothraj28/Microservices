package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.LoginRequestDTO;
import com.microservices.gateway.DTOS.RegisterRequestDTO;
import com.microservices.gateway.services.gRPCServices.UserGRPCService;
import com.microservices.profile.grpc.RegisterResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserGRPCService userGRPCService;

    public UserController(UserGRPCService userGRPCService) {
        this.userGRPCService = userGRPCService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDTO request
    ) {
        log.info("Fetching login via gRPC for the user {}", request.username());
        return ResponseEntity.ok(userGRPCService.login(request).getToken());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        log.info("Registering user via gRPC service {} ", registerRequestDTO.userName());
        return ResponseEntity.ok(userGRPCService.register(registerRequestDTO));
    }


}
