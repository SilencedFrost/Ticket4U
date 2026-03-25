package com.ticket4u.dto.auth;

import com.ticket4u.validation.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @NotBlank(message = "auth.password_reset.token_invalid")
        String token,

        @NotBlank(message = "auth.error.blank.password")
        @Pattern(message = "auth.error.format.password", regexp = ValidationPatterns.PASSWORD)
        String password
) {
}
