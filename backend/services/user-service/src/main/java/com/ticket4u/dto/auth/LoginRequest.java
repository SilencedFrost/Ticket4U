package com.ticket4u.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "auth.error.blank.email")
        @Email(message = "auth.error.format.email")
        String email,

        @NotBlank(message = "auth.error.blank.password")
        @Pattern(message = "auth.error.format.password", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$")
        String password,

        @NotNull(message = "Remember me can't be null")
        Boolean rememberMe
) {
}
