    package com.ticket4u.seatingmap.controller;

    import com.ticket4u.seatingmap.dto.SeatingPlanResponse;
    import com.ticket4u.seatingmap.service.SeatingMapServiceImpl;
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
    public class SeatingPlanController {

        private final SeatingMapServiceImpl seatingMapServiceImpl;

        @GetMapping("/{eventId}/seating-plan")
        public ResponseEntity<SeatingPlanResponse> getTicketSelectData(
                @PathVariable UUID eventId
        ) {
            return ResponseEntity.ok(seatingMapServiceImpl.getTicketSelectData(eventId));
        }
    }