package com.ticket4u.feature.homePage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for category information on homepage
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    
    private Integer id;
    
    private String name;
    
    private Integer eventCount;
}
