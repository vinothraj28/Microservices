package com.microservices.profile.controllers;

import com.microservices.profile.dto.user.LoginRequestDTO;
import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.dto.user.UserRequestDTO;
import com.microservices.profile.dto.user.UserRoleRequestDTO;
import com.microservices.profile.services.UserProfileService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/")
@Tag(name = "User Profile APIs")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "Fetch user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Found"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@Valid @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userProfileService.findByUserId(id)
        );
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Created"),
            @ApiResponse(responseCode = "409", description = "Email already exists."),
            @ApiResponse(responseCode = "422", description = "Validation failed")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Registering user with email: {}", userRequestDTO.emailAddress());
        UserResponseDTO response = userProfileService.addUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @Operation(summary = "Authenticate user and return JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Authentication successful, token returned"),
//            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
//            @ApiResponse(responseCode = "422", description = "Validation failed")
//    })
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
//        log.info("Login attempt for email: {}", loginRequest.username());
//        String token = userProfileService.login(loginRequest.username(), loginRequest.password());
//        return ResponseEntity.status(HttpStatus.OK).body(token);
//    }

    @Operation(summary = "Grant role to authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role granted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "422", description = "Validation failed")
    })
    @PostMapping("/roles")
    public ResponseEntity<String> addRole(@Valid @RequestBody UserRoleRequestDTO userRoleRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Adding role to user: {}", email);
        userProfileService.addRole(email, userRoleRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Role granted successfully");
    }

    @Operation(summary = "Extract and validate JWT token claims")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token claims extracted"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    @PostMapping("/token")
    public ResponseEntity<Claims> extractToken(@RequestBody String token) {
        log.debug("Extracting claims from token");
        return ResponseEntity.status(HttpStatus.OK).body(userProfileService.extractToken(token));
    }
}

