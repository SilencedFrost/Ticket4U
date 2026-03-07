package com.ticket4u.feature.homepage.service;

import com.ticket4u.core.dto.CategoryWithEventDto;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.dto.EventWithCategoryDto;
import com.ticket4u.core.mapper.EventSummaryMapper;
import com.ticket4u.feature.homepage.constants.Pagination;
import com.ticket4u.feature.homepage.dto.CategoryResponse;
import com.ticket4u.feature.homepage.dto.CategoryWithEventsResponse;
import com.ticket4u.feature.homepage.dto.PlaceResponse;
import com.ticket4u.feature.homepage.mapper.HomepageMapper;
import com.ticket4u.core.repository.CategoryRepository;
import com.ticket4u.core.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final HomepageMapper homepageMapper;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventSummaryMapper eventSummaryMapper;

    private int sanitizePage(Integer page) {
        return page == null ? Pagination.DEFAULT_PAGE : Math.max(page, 0);
    }

    private int sanitizeSize(Integer size) {
        return size == null ? Pagination.DEFAULT_SIZE : Math.min(size, Pagination.MAX_SIZE);
    }

    public List<EventSummaryResponse> getAllEventsWithMinPrice(Integer page, Integer size) {
        int sanitizedPage = sanitizePage(page);
        int sanitizedSize = sanitizeSize(size);

        int offset = sanitizedPage * sanitizedSize;

        List<EventWithCategoryDto> results = eventRepository.findEventsWithMinPrice(sanitizedSize, offset);
        return results.stream()
                .map(eventSummaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Lấy giá thấp nhất của 1 event cụ thể
    public Double getMinPriceForEvent(UUID eventId) {
        Double minPrice = eventRepository.findMinPriceByEventId(eventId);
        return minPrice != null ? minPrice : 0.0;
    }

    // Featured: Events mới nhất (newest 10)
    public List<EventSummaryResponse> getFeaturedEvents() {
        return eventRepository.findLatestEvents(10).stream()
                .map(eventSummaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Special: Events sắp diễn ra trong 7 ngày tới
    public List<EventSummaryResponse> getSpecialEvents() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return eventRepository.findEventsStartingBetween(now, now.plusDays(7), 10).stream()
                .map(eventSummaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Trending: Random 3 events
    public List<EventSummaryResponse> getTrendingEvents() {
        return eventRepository.findRandomEvents(3).stream()
                .map(eventSummaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Suggested: Random 10 events
    public List<EventSummaryResponse> getSuggestedEvents() {
        return eventRepository.findRandomEvents(10).stream()
                .map(eventSummaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Places: Return empty list for now (future implementation)
    public List<PlaceResponse> getPlaces() {
        return new ArrayList<>();
    }

    // Get all categories with their latest 4 events (single query, no N+1)
    public List<CategoryWithEventsResponse> getCategoriesWithEvents() {
        List<CategoryWithEventDto> rows = categoryRepository.findCategoriesWithLatestEvents();

        // Group flat rows by category, preserving order from the query (ORDER BY c.id)
        Map<Integer, CategoryWithEventsResponse> categoryMap = new LinkedHashMap<>();

        for (CategoryWithEventDto row : rows) {
            Integer categoryId = row.categoryId();
            String categoryName = row.categoryName();

            EventSummaryResponse event = new EventSummaryResponse(
                    row.eventId(), row.eventName(), row.bannerUrl(),
                    row.addressLine(), toOffsetDateTime(row.startDate()), toOffsetDateTime(row.endDate()),
                    row.minPrice(), null
            );

            categoryMap.computeIfAbsent(categoryId,
                    id -> new CategoryWithEventsResponse(id, categoryName, new ArrayList<>()))
                    .events().add(event);
        }

        return new ArrayList<>(categoryMap.values());
    }

    // Event Display: Get filtered events (supports multiple categories) with pagination
    public List<EventSummaryResponse> getFilteredEvents(
            LocalDate startDate, 
            LocalDate endDate, 
            List<Integer> categoryIds, 
            boolean isFreeOnly,
            int tzOffset,
            Integer page,
            Integer size
    ) {
        int sanitizedPage = sanitizePage(page);
        int sanitizedSize = sanitizeSize(size);

        int offset = sanitizedPage * sanitizedSize;

        // Convert LocalDate to OffsetDateTime using client timezone.
        // JS getTimezoneOffset() returns inverted sign: UTC+7 → -420, so negate it.
        ZoneOffset clientZone = ZoneOffset.ofTotalSeconds(-tzOffset * 60);

        OffsetDateTime startOdt = (startDate != null)
                ? startDate.atStartOfDay().atOffset(clientZone)
                : null;
        // endDate: use start-of-next-day so the entire end day is included
        OffsetDateTime endOdt = (endDate != null)
                ? endDate.plusDays(1).atStartOfDay().atOffset(clientZone)
                : null;
        
        // Check if category filter should be applied
        List<EventWithCategoryDto> results;
        if (categoryIds == null || categoryIds.isEmpty()) {
            // No category filter - get all events
            results = eventRepository.findEventsWithoutCategoryFilter(
                startOdt,
                endOdt,
                isFreeOnly,
                sanitizedSize,
                offset
            );
        } else {
            // Apply category filter
            results = eventRepository.findEventsWithCategoryFilter(
                startOdt,
                endOdt,
                categoryIds,
                isFreeOnly,
                sanitizedSize,
                offset
            );
        }
        
        return results.stream()
                .map(eventSummaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get all categories (for filter dropdown)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findCategoriesWithActiveEvents().stream()
                .map(homepageMapper::toDTO)
                .collect(Collectors.toList());
    }

    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
