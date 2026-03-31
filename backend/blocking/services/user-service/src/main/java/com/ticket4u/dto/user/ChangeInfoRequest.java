package com.ticket4u.dto.user;

import com.ticket4u.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ChangeInfoRequest(
        @Size(max = 32)
        String lastName,

        @Size(max = 32)
        String firstName,

        @Past
        LocalDate birthday,

        @ValidPhoneNumber(message = "auth.error.format.phone")
        String phoneNumber
) {}
