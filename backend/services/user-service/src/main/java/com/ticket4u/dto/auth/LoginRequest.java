package com.ticket4u.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "Email can't be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password can't be blank")
        @Pattern(message = "Password must contain one: lowercase letter, uppercase letter, special char (!@#$%^&*_-), and be 8-32 chars long", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$")
        String password,

        @NotNull(message = "Remember me can't be null")
        Boolean rememberMe
) {
}
