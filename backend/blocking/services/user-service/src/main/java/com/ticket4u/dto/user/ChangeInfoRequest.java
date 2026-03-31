package com.ticket4u.dto.user;

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

        @Pattern(regexp = "^[0-9]{10,15}$")
        String phoneNumber
) {}
