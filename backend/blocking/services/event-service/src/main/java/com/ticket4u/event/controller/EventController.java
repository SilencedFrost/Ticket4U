package com.ticket4u.event.controller;

import com.ticket4u.constants.DefaultParams;
import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.service.EventService;
import com.ticket4u.event.service.EventDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Controller for viewing event details in the Ticket4U system.
 * This class provides public APIs for users to browse event information.
 */
@RestController
@RequestMapping("/api/v1/public/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventDomainService eventDomainService;

    /**
     * GET /api/v1/public/events/{id}
     * Get full information of a specific event by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    /**
     * GET /api/v1/public/events/{id}/related
     * @param id used to identify relevancy and exclude self
     * @return a list of related event summaries based on category, city and upcoming event.
     */
    @GetMapping("/{id}/related")
    public ResponseEntity<List<EventSummaryResponse>> getRelatedEvents(@PathVariable UUID id) {
        return ResponseEntity.ok(eventDomainService.findRelatedEvents(id));
    }

    /**
     * GET /api/v1/public/events/featured?limit={limit}
     * @param limit the amount of featured events to get, by default is 4
     * @return a list of featured events with limited count
     */
    // TODO: implement better sampling logic based on business values such as large organizers, or paid promotion
    @GetMapping("/featured")
    public ResponseEntity<List<EventSummaryResponse>> getFeaturedEvents(@RequestParam(required = false, defaultValue = DefaultParams.FEATURED_COUNT_STRING) int limit) {
        return ResponseEntity.ok(eventDomainService.findUpcomingPurchasableEventsLimit(limit));
    }

    /**
     * GET /api/v1/public/events/locational?longitude={longitude}&latitude={latitude}
     * @param longitude longitude of the user collected from GPS data
     * @param latitude latitude of the user collected from GPS data
     * @return events within a specific distance, if no coordinates are provided, use IP coordinates
     */
    // TODO: implement event suggestion based on location
    @GetMapping("/locational")
    public ResponseEntity<List<EventSummaryResponse>> getLocationalEvents(
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double latitude
    ) {
        return ResponseEntity.ok(eventDomainService.findRandomEvent(10, 5));
    }

    @GetMapping("/trending")
    // TODO: implement event suggestion based on purchase count
    public ResponseEntity<List<EventSummaryResponse>> getTrendingEvents() {
        return ResponseEntity.ok(eventDomainService.findRandomEvent(3, 5));
    }

    @GetMapping("/suggested")
    // TODO implement event suggestion using ML and user behavior analysis
    public ResponseEntity<List<EventSummaryResponse>> getSuggestedEvents() {
        return ResponseEntity.ok(eventDomainService.findRandomEvent(10, 5));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<EventSummaryResponse>> getNearbyEvents(@RequestParam BigDecimal lat, @RequestParam BigDecimal lon) {
        return ResponseEntity.ok(eventDomainService.getNearbyEvents(lat, lon));
    }

    /**
     * GET /api/v1/public/events/search?q={query}&page={page}&size={size}
     * @param query the search keyword for semantic analysis
     * @param pageable pagination parameters (page, size, sort)
     * @return a page of events matching the semantic search query
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventSummaryResponse>> searchEvents(
            @RequestParam("q") String query,
            Pageable pageable
    ) {
        return ResponseEntity.ok(eventDomainService.searchEvents(query, pageable));
    }
}
