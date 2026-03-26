package com.ticket4u.dto.auth;

import com.ticket4u.validation.ValidEmailDomain;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "auth.error.blank.email")
        @Email(message = "auth.error.format.email")
        @ValidEmailDomain(message = "auth.error.format.email")
        String email
) {
}
