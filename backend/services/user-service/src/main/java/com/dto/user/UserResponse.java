package com.dto.user;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(

        UUID userId,
        String email,
        Integer roleId,
        String username,
        String firstName,
        String lastName,
        LocalDate birthday,
        String phoneNumber,
        OffsetDateTime updatedAt,
        OffsetDateTime createdAt
) {
}
