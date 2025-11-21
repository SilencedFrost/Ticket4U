package com.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserUpdateRequest(
        @NotNull(message = "User Id can't be null for update")
        Long userId,

        @Pattern(regexp = ".*\\S.*", message = "Email can't be blank")
        @Email(message = "Invalid email format")
        String email,

        @Pattern(regexp = ".*\\S.*", message = "Username can't be blank")
        String username,

        @Pattern(regexp = ".*\\S.*", message = "First name can't be blank")
        String firstName,

        @Pattern(regexp = ".*\\S.*", message = "Last name can't be blank")
        String lastName,

        @Past(message = "Birthday must be a past date")
        LocalDate birthday,

        @Pattern(message = "Password must contain one: lowercase letter, uppercase letter, special char (!@#$%^&*_-), and be 8-32 chars long", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$")
        String password,

        @Pattern(message = "Invalid phone number format", regexp = "^(0\\d{9}|[1-9]\\d{8})$")
        String phoneNumber,

        Boolean isActive
) {
}
