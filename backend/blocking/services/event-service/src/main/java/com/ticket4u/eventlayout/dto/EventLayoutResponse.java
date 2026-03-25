package com.ticket4u.eventlayout.dto;

import com.ticket4u.core.dto.ZoneResponse;

import java.util.List;

public record EventLayoutResponse(
        String layout,
        List<ZoneResponse> zones
) {}