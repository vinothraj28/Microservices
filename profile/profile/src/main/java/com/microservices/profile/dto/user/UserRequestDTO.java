package com.microservices.profile.dto.user;

import com.microservices.profile.models.enums.Roles;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record UserRequestDTO(

        @NotBlank
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
