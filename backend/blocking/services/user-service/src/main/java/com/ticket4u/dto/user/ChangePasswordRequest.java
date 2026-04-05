package com.ticket4u.dto.user;

import com.ticket4u.validation.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @NotBlank(message = "auth.error.blank.password")
        String currentPassword,

        @NotBlank(message = "auth.error.blank.password")
        @Pattern(message = "auth.error.format.password", regexp = ValidationPatterns.PASSWORD)
        String newPassword
) { }
