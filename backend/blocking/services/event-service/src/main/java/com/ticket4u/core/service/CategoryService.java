package com.ticket4u.core.service;

import com.ticket4u.core.dto.CategoryResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;

import java.util.List;

public interface CategoryService {
    List<CategorySummaryResponse> findAllCategories();
    CategoryResponse findTopUpcomingEventsInCategory(Integer id, Integer limit);
}
