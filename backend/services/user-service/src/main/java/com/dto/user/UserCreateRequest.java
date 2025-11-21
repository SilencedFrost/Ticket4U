package com.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserCreateRequest(

        @NotBlank(message = "Email can't be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Username can't be blank")
        String username,
        String firstName,
        String lastName,
        LocalDate birthday,

        @NotBlank(message = "Password can't be blank")
        @Pattern(message = "Password must contain one: lowercase letter, uppercase letter, special char (!@#$%^&*_-), and be 8-32 chars long", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$")
        String password,

        @NotBlank(message = "Phone number can't be blank")
        @Pattern(message = "Invalid phone number format", regexp = "^(0\\d{9}|[1-9]\\d{8})$")
        String phoneNumber,
        Integer roleId
) {
}
