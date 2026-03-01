package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ticket4u.feature.homepage.dto.EventCardResponse;

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
