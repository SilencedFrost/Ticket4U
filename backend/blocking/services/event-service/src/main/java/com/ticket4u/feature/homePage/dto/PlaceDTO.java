package com.ticket4u.feature.homePage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {
    private UUID id;
    private String name;
    private String imageUrl;
}
