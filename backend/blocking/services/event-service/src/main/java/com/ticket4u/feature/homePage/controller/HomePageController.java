package com.ticket4u.feature.homePage.controller;

import com.ticket4u.feature.homePage.dto.EventCardDTO;
import com.ticket4u.feature.homePage.dto.PlaceDTO;
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

    // GET /api/home/featured - Events nổi bật (mới nhất)
    @GetMapping("/featured")
    public ResponseEntity<List<EventCardDTO>> getFeaturedEvents() {
        List<EventCardDTO> events = homePageService.getFeaturedEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/special - Events đặc biệt (sắp diễn ra trong 7 ngày)
    @GetMapping("/special")
    public ResponseEntity<List<EventCardDTO>> getSpecialEvents() {
        List<EventCardDTO> events = homePageService.getSpecialEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/trending - Events xu hướng (random top 3)
    @GetMapping("/trending")
    public ResponseEntity<List<EventCardDTO>> getTrendingEvents() {
        List<EventCardDTO> events = homePageService.getTrendingEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/suggested - Events gợi ý (random)
    @GetMapping("/suggested")
    public ResponseEntity<List<EventCardDTO>> getSuggestedEvents() {
        List<EventCardDTO> events = homePageService.getSuggestedEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/music - Events âm nhạc
    @GetMapping("/music")
    public ResponseEntity<List<EventCardDTO>> getMusicEvents() {
        List<EventCardDTO> events = homePageService.getMusicEvents();
        return ResponseEntity.ok(events);
    }

    // GET /api/home/places - Danh sách địa điểm
    @GetMapping("/places")
    public ResponseEntity<List<PlaceDTO>> getPlaces() {
        List<PlaceDTO> places = homePageService.getPlaces();
        return ResponseEntity.ok(places);
    }
}
