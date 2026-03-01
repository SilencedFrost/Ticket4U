package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Response DTO representing a place on the homepage
public class PlaceResponse {
    private UUID id;
    private String name;
    private String imageUrl;
}
