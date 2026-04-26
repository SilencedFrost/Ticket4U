package com.ticket4u.core.service;

import com.ticket4u.core.dto.CategoryWithEventResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategorySummaryResponse> findAllCategories();
    CategoryWithEventResponse findTopUpcomingEventsInCategory(Integer id, Integer limit);
    List<CategoryWithEventResponse> findRecommendedCategories(UUID userId, Integer limit);
}
