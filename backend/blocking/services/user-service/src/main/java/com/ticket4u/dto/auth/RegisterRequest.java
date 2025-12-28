package com.ticket4u.dto.auth;

import com.ticket4u.validation.ValidPhoneNumber;
import com.ticket4u.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "auth.error.blank.email")
        @Email(message = "auth.error.format.email")
        String email,

        @NotBlank(message = "auth.error.blank.password")
        @Pattern(message = "auth.error.format.password", regexp = ValidationPatterns.PASSWORD)
        String password,

        @ValidPhoneNumber(message = "auth.error.format.phone")
        String phoneNumber,

        @Size(max = 100, message = "auth.error.size.fullName")
        String fullName
) {
}
