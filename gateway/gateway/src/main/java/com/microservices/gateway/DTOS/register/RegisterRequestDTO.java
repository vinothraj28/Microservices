package com.microservices.gateway.DTOS.register;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequestDTO(
        @NotBlank(message = "username should not be blank")
        @Size(min = 3, max = 50)
        String userName,

        @Email
        @NotBlank(message = "email address should not be blank")
        String emailAddress,

        @Past(message = "Date of birth must be in the past")
        LocalDate dob,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
                message = "Password must contain uppercase, lowercase and number"
        )
        String password
) {
}
