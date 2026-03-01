package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response DTO representing a place/venue on the homepage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponse {
    private UUID id;
    private String name;
    private String imageUrl;
}
