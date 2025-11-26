package com.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

public record UserUpdateRequest(
        @NotNull(message = "User Id can't be null for update")
        UUID id,

        @Pattern(regexp = ".*\\S.*", message = "Username can't be blank")
        String username,

        @Pattern(regexp = ".*\\S.*", message = "First name can't be blank")
        String firstName,

        @Pattern(regexp = ".*\\S.*", message = "Last name can't be blank")
        String lastName,

        @Past(message = "Birthday must be a past date")
        LocalDate birthday,

        @Pattern(message = "Password must contain one: lowercase letter, uppercase letter, special char (!@#$%^&*_-), and be 8-32 chars long", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$")
        String password
) {
}
