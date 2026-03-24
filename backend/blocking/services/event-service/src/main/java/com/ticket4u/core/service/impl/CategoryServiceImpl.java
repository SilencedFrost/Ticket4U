package com.ticket4u.core.service.impl;

import com.ticket4u.core.dto.CategoryResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.entity.Category;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.exceptions.CategoryNotFoundException;
import com.ticket4u.core.mapper.CategoryMapper;
import com.ticket4u.core.repository.CategoryRepository;
import com.ticket4u.core.repository.EventRepository;
import com.ticket4u.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategorySummaryResponse> findAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toSummaryDTO).toList();
    }

    /**
     * @param id id of the category
     * @param limit how many top upcoming events to search for, if limit = null, return unlimited
     * @return category response with the required event objects
     */
    @Override
    public CategoryResponse findTopUpcomingEventsInCategory(Integer id, Integer limit) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        List<Event> events = eventRepository.findUpcomingEventsByCategory(
                id,
                limit == null || limit <= 0 ? Pageable.unpaged() : PageRequest.of(0, limit)
        ).getContent();

        return categoryMapper.toDTO(category, events);
    }
}
