    package com.ticket4u.eventlayout.controller;

    import com.ticket4u.eventlayout.dto.EventLayoutResponse;
    import com.ticket4u.eventlayout.service.impl.EventLayoutServiceImpl;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.UUID;

    @RestController
    @RequestMapping("/api/v1/public/events")
    @RequiredArgsConstructor
    public class EventLayoutController {

        private final EventLayoutServiceImpl eventLayoutService;

        @GetMapping("/{eventId}/layout")
        public ResponseEntity<EventLayoutResponse> getLayout(@PathVariable UUID eventId) {
            return ResponseEntity.ok(eventLayoutService.getLayout(eventId));
        }
    }