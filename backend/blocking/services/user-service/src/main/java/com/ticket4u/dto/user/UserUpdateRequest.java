package com.ticket4u.dto.user;

import com.ticket4u.validation.ValidationPatterns;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

public record UserUpdateRequest(
        @NotNull(message = "User Id can't be null for update")
        UUID id,

        @Pattern(regexp = ValidationPatterns.NON_BLANK, message = "Username can't be blank")
        String username,

        @Pattern(regexp = ValidationPatterns.NON_BLANK, message = "First name can't be blank")
        String firstName,

        @Pattern(regexp = ValidationPatterns.NON_BLANK, message = "Last name can't be blank")
        String lastName,

        @Past(message = "Birthday must be a past date")
        LocalDate birthday,

        @Pattern(message = "Password must contain one: lowercase letter, uppercase letter, special char (!@#$%^&*_-), and be 8-32 chars long", regexp = ValidationPatterns.PASSWORD)
        String password
) {
}
