package com.ticket4u.event.service.impl;

import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.mapper.EventMapper;
import com.ticket4u.core.repository.EventRepository;
import com.ticket4u.core.service.EmbeddingService;
import com.ticket4u.core.service.QdrantService;
import com.ticket4u.event.service.EventSemanticService;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventSemanticServiceImpl implements EventSemanticService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;

    private static final String COLLECTION = "events";

    /**
     * Create and store a semantic vector for a specific event.
     * @param id The unique ID of the event to index.
     */
    @Override
    @Transactional(readOnly = true)
    public void storeEventVector(UUID id) {
        eventRepository.findById(id).ifPresent(event -> {
            String context = String.format("Category: %s | Event: %s | Description: %s",
                    event.getCategory().getName(),
                    event.getName(),
                    event.getAboutVi());

            List<Float> vector = embeddingService.getDocumentEmbedding(context);

            if (!vector.isEmpty()) {
                Common.PointId pointId = Common.PointId.newBuilder()
                        .setUuid(id.toString())
                        .build();

                Map<String, Object> payload = Map.of(
                        "categoryId", event.getCategory().getId().toString()
                );

                qdrantService.upsert(COLLECTION, pointId, vector, payload);
                log.info("Indexed semantic vector for event: {}", id);
            }
        });
    }

    /**
     * Find events with similar meanings based on vector distance.
     * @param id The ID of the current event to compare.
     * @param limit The number of similar events to return.
     * @return A list of similar events as DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventSummaryResponse> findSimilarById(UUID id, int limit) {
        return eventRepository.findById(id)
                .map(event -> {
                    String queryContext = String.format("Category: %s | Event: %s",
                            event.getCategory().getName(),
                            event.getName());
                    return embeddingService.getQueryEmbedding(queryContext);
                })
                .map(vector -> qdrantService.search(COLLECTION, vector, 0.3f, limit + 1))
                .map(scoredPoints -> extractAndSortIds(scoredPoints, id, limit))
                .map(this::fetchAndMapToDtos)
                .orElse(Collections.emptyList());
    }

    /**
     * Extract UUIDs from Qdrant results and remove the original event ID.
     * @param points Results from Qdrant.
     * @param excludeId The ID to remove from results.
     * @param limit Maximum IDs to return.
     * @return List of clean UUIDs.
     */
    private List<UUID> extractAndSortIds(List<Points.ScoredPoint> points, UUID excludeId, int limit) {
        return points.stream()
                .map(p -> UUID.fromString(p.getId().getUuid()))
                .filter(id -> !id.equals(excludeId))
                .limit(limit)
                .toList();
    }

    /**
     * Fetch event entities from database and convert them to response DTOs.
     * @param ids List of event IDs to fetch.
     * @return List of mapped EventSummaryResponse objects.
     */
    private List<EventSummaryResponse> fetchAndMapToDtos(List<UUID> ids) {
        if (ids.isEmpty()) return Collections.emptyList();

        Map<UUID, Event> eventMap = eventRepository.findAllByIdIn(ids).stream()
                .collect(Collectors.toMap(Event::getId, event -> event));

        return ids.stream()
                .map(eventMap::get)
                .filter(Objects::nonNull)
                .map(eventMapper::toSummaryDTO)
                .toList();
    }
}
