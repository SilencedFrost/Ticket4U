package com.ticket4u.feature.eventDetail.controller;

import com.ticket4u.feature.eventDetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventDetail.service.EventDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
