package com.ticket4u.feature.eventdetail.controller;

import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for viewing event details in the Ticket4U system.
 * This class provides public APIs for users to browse event information.
 */
@RestController
@RequestMapping("/api/v1/public/events")
@RequiredArgsConstructor
public class EventDetailController {
    private final EventDetailService eventDetailService;

    /**
     * GET /api/v1/public/events/{id}
     * Get full information of a specific event by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEventDetail(@PathVariable UUID id) {
        EventDetailResponse response = eventDetailService.getEventDetail(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/public/events/{id}/related
     * Get a list of related events based on category, city and upcoming event.
     * Helps users find similar shows or events nearby.
     * - id: to exclude the current event.
     * - categoryId: to find events in the same category.
     * - address: to find events in the same city.
     */
    @GetMapping("/{id}/related")
    public ResponseEntity<List<EventSummaryResponse>> getRelatedEvents(
            @PathVariable UUID id,
            @RequestParam Integer categoryId,
            @RequestParam String address
    ) {
        List<EventSummaryResponse> events = eventDetailService.getRelatedEvents(id, categoryId, address);
        return ResponseEntity.ok(events);
    }
}
