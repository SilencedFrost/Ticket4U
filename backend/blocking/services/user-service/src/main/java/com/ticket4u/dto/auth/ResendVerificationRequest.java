package com.ticket4u.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationRequest(
        @NotBlank(message = "auth.error.blank.email")
        @Email(message = "auth.error.format.email")
        String email
) {
}
