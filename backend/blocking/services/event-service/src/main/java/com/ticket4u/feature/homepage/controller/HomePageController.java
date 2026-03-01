package com.ticket4u.feature.homepage.controller;

import com.ticket4u.feature.homepage.dto.CategoryResponse;
import com.ticket4u.feature.homepage.dto.CategoryWithEventsResponse;
import com.ticket4u.feature.homepage.dto.EventCardResponse;
import com.ticket4u.feature.homepage.dto.PlaceResponse;
import com.ticket4u.feature.homepage.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/home")
@RequiredArgsConstructor
// @RateLimiter(name = "homePageLimiter")
public class HomePageController {

    private final HomePageService homePageService;

    // GET /api/home/events - Lấy danh sách events với giá thấp nhất
    @GetMapping("/events")
    public ResponseEntity<List<EventCardResponse>> getEventsWithMinPrice() {
        List<EventCardResponse> events = homePageService.getAllEventsWithMinPrice();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/events/{id}/min-price - Lấy giá thấp nhất của 1 event
    @GetMapping("/events/{id}/min-price")
    public ResponseEntity<Double> getMinPriceForEvent(@PathVariable UUID id) {
        Double minPrice = homePageService.getMinPriceForEvent(id);
        return ResponseEntity.ok(minPrice);
    }

    // GET /api/home/featured - Events nổi bật (mới nhất)
    @GetMapping("/featured")
    public ResponseEntity<List<EventCardResponse>> getFeaturedEvents() {
        List<EventCardResponse> events = homePageService.getFeaturedEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/special - Events đặc biệt (sắp diễn ra trong 7 ngày)
    @GetMapping("/special")
    public ResponseEntity<List<EventCardResponse>> getSpecialEvents() {
        List<EventCardResponse> events = homePageService.getSpecialEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/trending - Events xu hướng (random top 3)
    @GetMapping("/trending")
    public ResponseEntity<List<EventCardResponse>> getTrendingEvents() {
        List<EventCardResponse> events = homePageService.getTrendingEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/suggested - Events gợi ý (random)
    @GetMapping("/suggested")
    public ResponseEntity<List<EventCardResponse>> getSuggestedEvents() {
        List<EventCardResponse> events = homePageService.getSuggestedEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/music - Events âm nhạc
    @GetMapping("/music")
    public ResponseEntity<List<EventCardResponse>> getMusicEvents() {
        List<EventCardResponse> events = homePageService.getMusicEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/places - Danh sách địa điểm
    @GetMapping("/places")
    public ResponseEntity<List<PlaceResponse>> getPlaces() {
        List<PlaceResponse> places = homePageService.getPlaces();
        return ResponseEntity.ok(places);
    }

    // GET /api/home/categories - Get all categories with their latest 4 events
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryWithEventsResponse>> getCategoriesWithEvents() {
        List<CategoryWithEventsResponse> categories = homePageService.getCategoriesWithEvents();
        return ResponseEntity.ok(categories);
    }

    // GET /api/home/categories/all - Get all categories for filter dropdown
    @GetMapping("/categories/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = homePageService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // GET /api/home/events/filter - Get filtered events for event-display page (supports multiple categories)
    @GetMapping("/events/filter")
    public ResponseEntity<List<EventCardResponse>> getFilteredEvents(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) Boolean isFreeOnly,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        List<EventCardResponse> events = homePageService.getFilteredEvents(startDate, endDate, categoryIds, isFreeOnly, page, size);
        return ResponseEntity.ok(events);
    }
}

