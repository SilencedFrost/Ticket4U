package com.ticket4u.feature.homepage.dto;

import java.util.UUID;

/**
 * Response DTO representing a place/venue on the homepage
 */
public record PlaceResponse(
        UUID id,
        String name,
        String imageUrl
) {}
