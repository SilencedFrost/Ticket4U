package com.ticket4u.core.controller;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
