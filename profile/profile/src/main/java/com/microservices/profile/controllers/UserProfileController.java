package com.microservices.profile.controllers;

import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.dto.user.UserRequestDTO;
import com.microservices.profile.dto.user.UserRoleRequestDTO;
import com.microservices.profile.services.Impl.UserProfileServiceImpl;
import com.microservices.profile.services.UserProfileService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/")
@Tag(name = "User Profile APIs")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "Fetch user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description = "User Found"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@Valid @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(
                    userProfileService.findByUserId(id)
            );
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Created"),
            @ApiResponse(responseCode = "409", description = "Email already exists.")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO response =
                userProfileService.addUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials){
        String email = credentials.get("username");
        String password = credentials.get("password");
        if(email!=null && !email.isBlank() && password!=null && !password.isBlank()){
            String token = userProfileService.login(email, password);
            if(!token.equals("Username/Password incorrect")){
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username/Password incorrect");
    }

    @PostMapping("/roles")
    public ResponseEntity<String> addRole(@Valid @RequestBody UserRoleRequestDTO userRoleRequestDTO){

        System.out.println("Request to add roles");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username");
        if (userProfileService.addRole(username, userRoleRequestDTO)){
            return ResponseEntity.status(HttpStatus.OK).body("roles are added to the user");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add roles");
    }

    @Operation(summary = "Extract token details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token details"),
            @ApiResponse(responseCode = "401", description = "Token expired")
    })
    @PostMapping("/token")
    public ResponseEntity<Claims> extractToken(@RequestBody String token ){
        return ResponseEntity.status(HttpStatus.OK).body(userProfileService.extractToken(token));
    }


}
