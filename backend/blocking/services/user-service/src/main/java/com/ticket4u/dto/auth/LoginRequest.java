package com.ticket4u.dto.auth;

import com.ticket4u.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "auth.error.blank.email")
        @Email(message = "auth.error.format.email")
        String email,

        @NotBlank(message = "auth.error.blank.password")
        @Pattern(message = "auth.error.format.password", regexp = ValidationPatterns.PASSWORD)
        String password,

        @NotNull(message = "Remember me can't be null")
        Boolean rememberMe
) {
}
