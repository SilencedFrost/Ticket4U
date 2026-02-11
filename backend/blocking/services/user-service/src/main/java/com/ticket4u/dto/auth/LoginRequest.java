package com.ticket4u.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank(message = "auth.error.blank.email")
        String email,

        @NotBlank(message = "auth.error.blank.password")
        String password,

        @NotNull(message = "Remember me can't be null")
        Boolean rememberMe
) {
}
