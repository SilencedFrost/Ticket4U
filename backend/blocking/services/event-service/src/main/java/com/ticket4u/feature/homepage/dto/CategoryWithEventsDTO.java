package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for category with its events
 * Used for homepage category sections
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithEventsDTO {
    private Integer id;
    private String name;
    private List<EventCardDTO> events;
}
