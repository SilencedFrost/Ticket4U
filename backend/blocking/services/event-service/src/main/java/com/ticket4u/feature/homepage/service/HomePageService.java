package com.ticket4u.feature.homepage.service;

import com.ticket4u.feature.homepage.dto.CategoryDTO;
import com.ticket4u.feature.homepage.dto.CategoryWithEventsDTO;
import com.ticket4u.feature.homepage.dto.EventCardDTO;
import com.ticket4u.feature.homepage.dto.PlaceDTO;
import com.ticket4u.feature.homepage.mapper.NativeQueryMapper;
import com.ticket4u.feature.homepage.repository.CategoryRepository;
import com.ticket4u.feature.homepage.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final NativeQueryMapper nativeQueryMapper;

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_EVENTS_PER_CATEGORY = 4; // For homepage display

    // Lấy tất cả events với giá thấp nhất
    public List<EventCardDTO> getAllEventsWithMinPrice() {
        List<Object[]> results = eventRepository.findEventsWithMinPrice();
        return results.stream()
                .map(nativeQueryMapper::toEventCardDTO)
                .collect(Collectors.toList());
    }

    // Lấy giá thấp nhất của 1 event cụ thể
    public Double getMinPriceForEvent(UUID eventId) {
        Double minPrice = eventRepository.findMinPriceByEventId(eventId);
        return minPrice != null ? minPrice : 0.0;
    }

    // Featured: Events mới nhất
    public List<EventCardDTO> getFeaturedEvents() {
        List<Object[]> results = eventRepository.findFeaturedEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventCardDTO)
                .collect(Collectors.toList());
    }

    // Special: Events sắp diễn ra trong 7 ngày
    public List<EventCardDTO> getSpecialEvents() {
        List<Object[]> results = eventRepository.findSpecialEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventCardDTO)
                .collect(Collectors.toList());
    }

    // Trending: Random 3 PLANNED/ONGOING events
    public List<EventCardDTO> getTrendingEvents() {
        List<Object[]> results = eventRepository.findTrendingEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventCardDTO)
                .collect(Collectors.toList());
    }

    // Suggested: Random PLANNED/ONGOING events
    public List<EventCardDTO> getSuggestedEvents() {
        List<Object[]> results = eventRepository.findSuggestedEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventCardDTO)
                .collect(Collectors.toList());
    }

     // Music: Events với category = 'Music'
     public List<EventCardDTO> getMusicEvents() {
         List<Object[]> results = eventRepository.findEventsByCategory("Âm nhạc (Concert)");
         return results.stream()
                 .map(nativeQueryMapper::toEventCardDTO)
                 .collect(Collectors.toList());
     }

    // Places: Return empty list for now (future implementation)
    public List<PlaceDTO> getPlaces() {
        return new ArrayList<>();
    }

    // Get all categories with their latest 4 events
    public List<CategoryWithEventsDTO> getCategoriesWithEvents() {
        List<Object[]> categoryResults = categoryRepository.findCategoriesWithActiveEvents();
        
        return categoryResults.stream().map(categoryRow -> {
            Integer categoryId = (Integer) categoryRow[0];
            String categoryName = (String) categoryRow[1];
            
            // Get latest 4 events for this category
            List<Object[]> eventResults = eventRepository.findLatestEventsByCategoryId(categoryId);
            List<EventCardDTO> events = eventResults.stream()
                    .map(nativeQueryMapper::toEventCardDTO)
                    .limit(MAX_EVENTS_PER_CATEGORY)
                    .collect(Collectors.toList());
            
            return new CategoryWithEventsDTO(categoryId, categoryName, events);
        }).collect(Collectors.toList());
    }

    // Event Display: Get filtered events (supports multiple categories) with pagination
    public List<EventCardDTO> getFilteredEvents(
            String startDate, 
            String endDate, 
            List<Integer> categoryIds, 
            Boolean isFreeOnly,
            Integer page,
            Integer size
    ) {
        Boolean effectiveIsFreeOnly = (isFreeOnly != null) ? isFreeOnly : false;
        int effectivePage = (page != null && page >= 0) ? page : 0;
        int effectiveSize = (size != null && size > 0 && size <= MAX_PAGE_SIZE) ? size : DEFAULT_PAGE_SIZE;
        
        int offset = effectivePage * effectiveSize;
        
        // Check if category filter should be applied
        List<Object[]> results;
        if (categoryIds == null || categoryIds.isEmpty()) {
            // No category filter - get all events
            results = eventRepository.findEventsWithoutCategoryFilter(
                startDate,
                endDate,
                effectiveIsFreeOnly,
                effectiveSize,
                offset
            );
        } else {
            // Apply category filter
            results = eventRepository.findEventsWithCategoryFilter(
                startDate,
                endDate,
                categoryIds,
                effectiveIsFreeOnly,
                effectiveSize,
                offset
            );
        }
        
        return results.stream()
                .map(nativeQueryMapper::toEventCardDTO)
                .collect(Collectors.toList());
    }

    // Get all categories (for filter dropdown)
    public List<CategoryDTO> getAllCategories() {
        List<Object[]> results = categoryRepository.findCategoriesWithActiveEvents();
        return results.stream()
                .map(nativeQueryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }
}