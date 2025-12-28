package com.ticket4u.dto.user;

import com.ticket4u.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserCreateRequest(

        @NotBlank(message = "Email can't be blank")
        @Email(message = "Invalid email format")
        String email,

        Integer roleId,

        @NotBlank(message = "Username can't be blank")
        String username,

        String firstName,
        String lastName,
        LocalDate birthday,

        @NotBlank(message = "Password can't be blank")
        @Pattern(message = "Password must contain one: lowercase letter, uppercase letter, special char (!@#$%^&*_-), and be 8-32 chars long", regexp = ValidationPatterns.PASSWORD)
        String password,

        @NotBlank(message = "Phone number can't be blank")
        @Pattern(message = "Invalid phone number format", regexp = ValidationPatterns.PHONE_NUMBER)
        String phoneNumber
) {
}
