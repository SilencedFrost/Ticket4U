package com.ticket4u.feature.eventDetail.controller;

import com.ticket4u.feature.eventDetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventDetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventCardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/public/events")
@RequiredArgsConstructor
public class EventDetailController {
    private final EventDetailService eventDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEventDetail(@PathVariable UUID id) {
        EventDetailResponse response = eventDetailService.getEventDetail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<List<EventCardDTO>> getRelatedEvents(
            @PathVariable UUID id,
            @RequestParam Integer categoryId,
            @RequestParam String address
    ) {
        List<EventCardDTO> events = eventDetailService.getRelatedEvents(id, categoryId, address);
        return ResponseEntity.ok(events);
    }
}
