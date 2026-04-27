package com.ticket4u.core.controller;

import com.ticket4u.core.dto.CategoryWithEventResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.entity.CustomUserDetails;
import com.ticket4u.core.service.CategoryService;
import com.ticket4u.util.AuthPrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<CategoryWithEventResponse> getCategoryWithEvents(
            @PathVariable(name = "category-id") Integer categoryId,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(categoryService.findTopUpcomingEventsInCategory(categoryId, limit));
    }

    /**
     * GET /api/v1/public/categories/recommended
     */
    @GetMapping("/recommended")
    public ResponseEntity<List<CategoryWithEventResponse>> getRecommendedCategoriesWithEvents(@RequestParam(required = false) Integer limit, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = AuthPrincipalUtil.extractUserIdSafe(userDetails);
        return ResponseEntity.ok(categoryService.findRecommendedCategories(userId, limit));
    }
}
