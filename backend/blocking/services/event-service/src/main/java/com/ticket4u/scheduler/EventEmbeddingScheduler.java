package com.ticket4u.scheduler;

import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.service.EventService;
import com.ticket4u.embedding.service.EventSemanticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventEmbeddingScheduler {

    private final EventService eventService;
    private final EventSemanticService eventSemanticService;

    @Scheduled(fixedDelay = 3600000, initialDelay = 5000)
    @Transactional
    public void embedAllEvent() {
        log.info("Starting embed task for events");
        List<EventSummaryResponse> events = eventService.findAll();
        List<UUID> eventIds = events.stream().map(EventSummaryResponse::id).toList();
        eventSemanticService.storeEventVectors(eventIds, false);
        log.info("Finished embedding any service that does not have an embedding");
    }
}
