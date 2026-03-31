package com.ticket4u.dto.session;

import java.time.OffsetDateTime;

public record SessionResponse(
        String displayId,
        String userAgent,
        OffsetDateTime updateAt
) {}
