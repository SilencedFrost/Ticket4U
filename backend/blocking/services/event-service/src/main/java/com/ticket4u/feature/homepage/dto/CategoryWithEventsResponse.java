package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for category with its events
 * Used for homepage category sections
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithEventsResponse {
    private Integer id;
    private String name;
    private List<EventCardResponse> events;
}
