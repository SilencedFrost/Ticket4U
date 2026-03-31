package com.ticket4u.dto.user;


import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        LocalDate birthday,
        String phoneNumber,
        OffsetDateTime createdAt

        /** TODO:
         * String avatarUrl
         * String memberTier (Optional, we can add if necessary)
         */
) {}
