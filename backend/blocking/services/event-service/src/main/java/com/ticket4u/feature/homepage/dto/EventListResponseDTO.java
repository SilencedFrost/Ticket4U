package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for homepage event list response
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventListResponseDTO {
    
    private List<EventCardDTO> events;
    
    private Integer totalElements;
    
    private Integer totalPages;
    
    private Integer currentPage;
    
    private Integer pageSize;
}
