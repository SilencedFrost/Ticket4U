package com.ticket4u.core.controller;

import com.ticket4u.core.dto.CategoryResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * GET /api/v1/public/categories
     */
    @GetMapping
    public ResponseEntity<List<CategorySummaryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    /**
     * GET /api/v1/public/categories/{category-id}/events/upcoming?limit={limit}
     */
    @GetMapping("/{category-id}/events/upcoming")
    public ResponseEntity<CategoryResponse> getCategoryWithEvents(
            @PathVariable(name = "category-id") Integer categoryId,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(categoryService.findTopUpcomingEventsInCategory(categoryId, limit));
    }
}
