package com.ticket4u.feature.homepage.service;

import com.ticket4u.feature.homepage.dto.CategoryResponse;
import com.ticket4u.feature.homepage.dto.CategoryWithEventsResponse;
import com.ticket4u.feature.homepage.dto.EventSummaryResponse;
import com.ticket4u.feature.homepage.dto.PlaceResponse;
import com.ticket4u.feature.homepage.mapper.NativeQueryMapper;
import com.ticket4u.feature.homepage.projection.CategoryWithEventProjection;
import com.ticket4u.feature.homepage.projection.EventSummaryProjection;
import com.ticket4u.feature.homepage.projection.EventWithCategoryProjection;
import com.ticket4u.feature.homepage.repository.CategoryRepository;
import com.ticket4u.feature.homepage.repository.EventRepository;
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
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final NativeQueryMapper nativeQueryMapper;

    // Lấy tất cả events với giá thấp nhất (paginated)
    public List<EventSummaryResponse> getAllEventsWithMinPrice(int page, int size) {
        int offset = page * size;

        List<EventSummaryProjection> results = eventRepository.findEventsWithMinPrice(size, offset);
        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .collect(Collectors.toList());
    }

    // Lấy giá thấp nhất của 1 event cụ thể
    public Double getMinPriceForEvent(UUID eventId) {
        Double minPrice = eventRepository.findMinPriceByEventId(eventId);
        return minPrice != null ? minPrice : 0.0;
    }

    // Featured: Events mới nhất
    public List<EventSummaryResponse> getFeaturedEvents() {
        List<EventSummaryProjection> results = eventRepository.findFeaturedEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .collect(Collectors.toList());
    }

    // Special: Events sắp diễn ra trong 7 ngày
    public List<EventSummaryResponse> getSpecialEvents() {
        List<EventSummaryProjection> results = eventRepository.findSpecialEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .collect(Collectors.toList());
    }

    // Trending: Random 3 PLANNED/ONGOING events
    public List<EventSummaryResponse> getTrendingEvents() {
        List<EventSummaryProjection> results = eventRepository.findTrendingEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .collect(Collectors.toList());
    }

    // Suggested: Random PLANNED/ONGOING events
    public List<EventSummaryResponse> getSuggestedEvents() {
        List<EventSummaryProjection> results = eventRepository.findSuggestedEvents();
        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .collect(Collectors.toList());
    }

    // Places: Return empty list for now (future implementation)
    public List<PlaceResponse> getPlaces() {
        return new ArrayList<>();
    }

    // Get all categories with their latest 4 events (single query, no N+1)
    public List<CategoryWithEventsResponse> getCategoriesWithEvents() {
        List<CategoryWithEventProjection> rows = categoryRepository.findCategoriesWithLatestEvents();

        // Group flat rows by category, preserving order from the query (ORDER BY c.id)
        Map<Integer, CategoryWithEventsResponse> categoryMap = new LinkedHashMap<>();

        for (CategoryWithEventProjection row : rows) {
            Integer categoryId = row.getCategoryId();
            String categoryName = row.getCategoryName();

            EventSummaryResponse event = new EventSummaryResponse(
                    row.getEventId(), row.getEventName(), row.getBannerUrl(),
                    row.getAddressLine(), toOffsetDateTime(row.getStartDate()), toOffsetDateTime(row.getEndDate()),
                    row.getMinPrice(), null
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
            int page,
            int size
    ) {
        int offset = page * size;

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
        List<EventWithCategoryProjection> results;
        if (categoryIds == null || categoryIds.isEmpty()) {
            // No category filter - get all events
            results = eventRepository.findEventsWithoutCategoryFilter(
                startOdt,
                endOdt,
                isFreeOnly,
                size,
                offset
            );
        } else {
            // Apply category filter
            results = eventRepository.findEventsWithCategoryFilter(
                startOdt,
                endOdt,
                categoryIds,
                isFreeOnly,
                size,
                offset
            );
        }
        
        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .collect(Collectors.toList());
    }

    // Get all categories (for filter dropdown)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findCategoriesWithActiveEvents().stream()
                .map(nativeQueryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
