package com.dto.user;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record UserResponse(
        Long userId,
        String email,
        String username,
        String firstName,
        String lastName,
        LocalDate birthday,
        String phoneNumber,
        Integer roleId,
        Boolean isActive,
        OffsetDateTime creationDate,
        OffsetDateTime updatedAt
) {
}
