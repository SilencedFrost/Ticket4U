package com.ticket4u.dto.user;

import com.ticket4u.validation.ValidEmailDomain;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequest(
        @NotBlank(message = "auth.error.blank.email")
        @Email(message = "auth.error.format.email")
        @ValidEmailDomain(message = "auth.error.format.email")
        String newEmail,

        @NotBlank(message = "auth.error.blank.password")
        String currentPassword
) {}
