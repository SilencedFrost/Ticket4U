package com.ticket4u.feature.homePage.service;

import com.ticket4u.feature.homePage.dto.CategoryDTO;
import com.ticket4u.feature.homePage.dto.CategoryWithEventsDTO;
import com.ticket4u.feature.homePage.dto.EventCardDTO;
import com.ticket4u.feature.homePage.dto.PlaceDTO;
import com.ticket4u.feature.homePage.repository.CategoryRepository;
import com.ticket4u.feature.homePage.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageService {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    CategoryRepository categoryRepository;

    // Lấy tất cả events với giá thấp nhất
    public List<EventCardDTO> getAllEventsWithMinPrice() {
        List<Object[]> results = eventRepository.findEventsWithMinPrice();
        return results.stream()
                .map(EventCardDTO::new)
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
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Special: Events sắp diễn ra trong 7 ngày
    public List<EventCardDTO> getSpecialEvents() {
        List<Object[]> results = eventRepository.findSpecialEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Trending: Random 3 PLANNED/ONGOING events
    public List<EventCardDTO> getTrendingEvents() {
        List<Object[]> results = eventRepository.findTrendingEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Suggested: Random PLANNED/ONGOING events
    public List<EventCardDTO> getSuggestedEvents() {
        List<Object[]> results = eventRepository.findSuggestedEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Music: Events với category = 'Music'
    public List<EventCardDTO> getMusicEvents() {
        CategoryDTO categoryDTO = new CategoryDTO();
        List<Object[]> results = eventRepository.findEventsByCategory("Âm nhạc (Concert)");
        return results.stream()
                .map(EventCardDTO::new)
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
                    .map(EventCardDTO::new)
                    .collect(Collectors.toList());
            
            return new CategoryWithEventsDTO(categoryId, categoryName, events);
        }).collect(Collectors.toList());
    }

    // Event Display: Get filtered events (supports multiple categories)
    public List<EventCardDTO> getFilteredEvents(String startDate, String endDate, List<Integer> categoryIds, Boolean isFreeOnly) {
        Boolean effectiveIsFreeOnly = (isFreeOnly != null) ? isFreeOnly : false;
        
        // Check if category filter should be applied
        List<Object[]> results;
        if (categoryIds == null || categoryIds.isEmpty()) {
            // No category filter - get all events
            results = eventRepository.findEventsWithoutCategoryFilter(
                startDate,
                endDate,
                effectiveIsFreeOnly
            );
        } else {
            // Apply category filter
            results = eventRepository.findEventsWithCategoryFilter(
                startDate,
                endDate,
                categoryIds,
                effectiveIsFreeOnly
            );
        }
        
        return results.stream()
                .map(result -> {
                    EventCardDTO dto = new EventCardDTO(result);
                    // result[7] is category_name from the query
                    if (result.length > 7 && result[7] != null) {
                        dto.setCategoryName((String) result[7]);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Get all categories (for filter dropdown)
    public List<CategoryDTO> getAllCategories() {
        List<Object[]> results = categoryRepository.findCategoriesWithActiveEvents();
        return results.stream()
                .map(result -> {
                    Integer id = (Integer) result[0];
                    String name = (String) result[1];
                    return new CategoryDTO(id, name, null);
                })
                .collect(Collectors.toList());
    }
}
