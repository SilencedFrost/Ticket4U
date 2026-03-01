package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ticket4u.feature.homepage.dto.EventCardResponse;

/**
 * Response DTO for homepage event list
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventListResponse {
    
    private List<EventCardResponse> events;
    
    private Integer totalElements;
    
    private Integer totalPages;
    
    private Integer currentPage;
    
    private Integer pageSize;
}
