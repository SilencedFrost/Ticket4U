package com.ticket4u.dto.user;

import com.ticket4u.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ChangeInfoRequest(
        @Pattern(message = "auth.error.blank.lastName", regexp = ".*\\S.*")
        @Size(max = 32)
        String lastName,

        @Pattern(message = "auth.error.blank.firstName", regexp = ".*\\S.*")
        @Size(max = 32)
        String firstName,

        @Past
        LocalDate birthday,

        @ValidPhoneNumber(message = "auth.error.format.phone")
        String phoneNumber
) {}
