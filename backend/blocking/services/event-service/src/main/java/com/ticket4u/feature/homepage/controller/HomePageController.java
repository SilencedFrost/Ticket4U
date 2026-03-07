package com.ticket4u.feature.homepage.controller;

import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.feature.homepage.constants.Pagination;
import com.ticket4u.feature.homepage.dto.CategoryResponse;
import com.ticket4u.feature.homepage.dto.CategoryWithEventsResponse;

import com.ticket4u.feature.homepage.dto.PlaceResponse;
import com.ticket4u.feature.homepage.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/v1/public/home")
@RequiredArgsConstructor
// @RateLimiter(name = "homePageLimiter")
public class HomepageController {
    private final HomePageService homePageService;

    /**
     * GET /api/v1/public/home/events
     * Get a paginated list of events with min price
     *
     * @param page Page number
     * @param size Number of items per page
     * @return Paginated list of events
     */
    @GetMapping("/events")
    public ResponseEntity<List<EventSummaryResponse>> getEventsWithMinPrice(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(homePageService.getAllEventsWithMinPrice(page, size));
    }

    /**
     * GET /api/v1/public/home/events/{id}/min-price
     * Get the minimum price of a specific event
     * 
     * @param id Event ID to get minimum price for
     * @return Minimum ticket price for the event
     */
    @GetMapping("/events/{id}/min-price")
    public ResponseEntity<Double> getMinPriceForEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(homePageService.getMinPriceForEvent(id));
    }

    /**
     * GET /api/v1/public/home/featured
     * Get featured events (latest events)
     * 
     * @return List of featured events
     */
    @GetMapping("/featured")
    public ResponseEntity<List<EventSummaryResponse>> getFeaturedEvents() {
        return ResponseEntity.ok(homePageService.getFeaturedEvents());
    }

    /**
     * GET /api/v1/public/home/special
     * Get special events (upcoming within 7 days)
     * 
     * @return List of events starting within the next 7 days
     */
    @GetMapping("/special")
    public ResponseEntity<List<EventSummaryResponse>> getSpecialEvents() {
        return ResponseEntity.ok(homePageService.getSpecialEvents());
    }

    /**
     * GET /api/v1/public/home/trending
     * Get trending events (random top 3 events)
     * 
     * @return List of 3 randomly selected trending events
     */
    @GetMapping("/trending")
    public ResponseEntity<List<EventSummaryResponse>> getTrendingEvents() {
        return ResponseEntity.ok(homePageService.getTrendingEvents());
    }

    /**
     * GET /api/v1/public/home/suggested
     * Get suggested events (random selection)
     * 
     * @return List of randomly suggested events
     */
    @GetMapping("/suggested")
    public ResponseEntity<List<EventSummaryResponse>> getSuggestedEvents() {
        return ResponseEntity.ok(homePageService.getSuggestedEvents());
    }

    /**
     * GET /api/v1/public/home/places
     * Get list of places/venues
     * 
     * @return List of event venues
     */
    @GetMapping("/places")
    public ResponseEntity<List<PlaceResponse>> getPlaces() {
        return ResponseEntity.ok(homePageService.getPlaces());
    }

    /**
     * GET /api/v1/public/home/categories
     * Get all categories with their latest 4 events
     * 
     * @return List of categories, each containing up to 4 latest events
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryWithEventsResponse>> getCategoriesWithEvents() {
        return ResponseEntity.ok(homePageService.getCategoriesWithEvents());
    }

    /**
     * GET /api/v1/public/home/categories/all
     * Get all categories for filter dropdown
     * 
     * @return List of all event categories
     */
    @GetMapping("/categories/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(homePageService.getAllCategories());
    }

    /**
     * GET /api/v1/public/home/events/filter
     * Get filtered events for event-display page (supports multiple categories)
     * 
     * @param startDate Filter events starting from this date (format: yyyy-MM-dd)
     * @param endDate Filter events ending before this date (format: yyyy-MM-dd)
     * @param categoryIds List of category IDs to filter by (supports multiple)
     * @param isFreeOnly Filter to show only free events (true/false)
     * @param page Page number for pagination (default: 0)
     * @param size Number of items per page (default: 20)
     * @return Paginated list of filtered events
     */
    @GetMapping("/events/filter")
    public ResponseEntity<List<EventSummaryResponse>> getFilteredEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false, defaultValue = "false") boolean isFreeOnly,
            @RequestParam(required = false) Integer tzOffset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(homePageService.getFilteredEvents(startDate, endDate, categoryIds, isFreeOnly, tzOffset, page, size));
    }
}

