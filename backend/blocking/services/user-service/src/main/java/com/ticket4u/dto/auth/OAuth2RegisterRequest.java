package com.ticket4u.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record OAuth2RegisterRequest(
        @NotBlank(message = "auth.error.blank.googleToken")
        String idToken,
        String fullName
) {
}
