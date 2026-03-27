package com.ticket4u.core.dto;

import java.util.List;

public record LayoutResponse(
        String layout,
        List<ZoneResponse> zones
) {}