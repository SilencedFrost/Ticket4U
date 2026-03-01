package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for category information on homepage
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    
    private Integer id;
    
    private String name;
    
    private Integer eventCount;
}
