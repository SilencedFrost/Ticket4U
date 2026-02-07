package com.ticket4u.feature.homePage.controller;

import com.ticket4u.feature.homePage.dto.EventCardDTO;
import com.ticket4u.feature.homePage.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomePageController {
    
    private final HomePageService homePageService;
    
    // GET /api/home/events - Lấy danh sách events với giá thấp nhất
    @GetMapping("/events")
    public ResponseEntity<List<EventCardDTO>> getEventsWithMinPrice() {
        List<EventCardDTO> events = homePageService.getAllEventsWithMinPrice();
        return ResponseEntity.ok(events);
    }
    
    // GET /api/home/events/{id}/min-price - Lấy giá thấp nhất của 1 event
    @GetMapping("/events/{id}/min-price")
    public ResponseEntity<Double> getMinPriceForEvent(@PathVariable UUID id) {
        Double minPrice = homePageService.getMinPriceForEvent(id);
        return ResponseEntity.ok(minPrice);
    }
}
