package com.ticket4u.core.service.impl;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.mapper.CategoryMapper;
import com.ticket4u.core.repository.CategoryRepository;
import com.ticket4u.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategorySummaryResponse> findAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toSummaryDTO).toList();
    }
}
