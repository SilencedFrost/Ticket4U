package com.ticket4u.embedding.service.impl;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.embedding.service.EmbeddingService;
import com.ticket4u.core.service.EventService;
import com.ticket4u.embedding.service.QdrantService;
import com.ticket4u.embedding.service.EventSemanticService;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventSemanticServiceImpl implements EventSemanticService {

    private final EventService eventService;
    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;

    private static final String COLLECTION = "events";
    private static final String PASSAGE_TEXT = "Event: %s | Categories: %s | Venue: %s | Description: %s";
    private static final float SIMILARITY_THRESHOLD = 0.3f;

    private String createPassage(EventResponse event) {
        return String.format(
                PASSAGE_TEXT,
                event.name(),
                event.categories().stream().map(CategorySummaryResponse::name).collect(Collectors.joining(", ")),
                event.addressLine(),
                event.aboutEn().isEmpty() ? event.aboutVi() : event.aboutEn()
        );
    }

    /**
     * Create and store a semantic vector for a specific event.
     * @param id The unique ID of the event to index.
     */
    @Override
    @Transactional(readOnly = true)
    public void storeEventVector(UUID id) {
        EventResponse event = eventService.findById(id);

        String passage = createPassage(event);

        List<Float> vector = embeddingService.embed(passage, EmbeddingService.MODE.TEXT);

        if (!vector.isEmpty()) {
            Common.PointId pointId = Common.PointId.newBuilder()
                    .setUuid(id.toString())
                    .build();

            qdrantService.upsert(COLLECTION, pointId, vector, Collections.emptyMap());
            log.info("Indexed semantic vector for event: {}", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void storeEventVectors(List<UUID> ids, boolean override) {
        List<UUID> toProcess = ids;

        if (!override) {
            Set<String> existingIds = qdrantService.filterExistingIds(COLLECTION,
                            ids.stream()
                                    .map(id -> Common.PointId.newBuilder().setUuid(id.toString()).build())
                                    .collect(Collectors.toList())
                    ).stream()
                    .map(Common.PointId::getUuid)
                    .collect(Collectors.toSet());

            toProcess = ids.stream()
                    .filter(id -> !existingIds.contains(id.toString()))
                    .toList();

            log.info("Override=false: {}/{} IDs already indexed, processing {} new",
                    existingIds.size(), ids.size(), toProcess.size());
        }

        if (toProcess.isEmpty()) {
            log.info("No events to index, skipping upsert.");
            return;
        }

        // Build passage map: UUID -> passage text
        Map<Object, String> passageMap = toProcess.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> createPassage(eventService.findById(id))
                ));

        // Batch embed all passages at once
        Map<Object, List<Float>> vectorMap = embeddingService.embed(passageMap, EmbeddingService.MODE.TEXT);

        // Build batch entries, skipping empty vectors
        List<QdrantService.UpsertEntry> entries = vectorMap.entrySet().stream()
                .filter(entry -> {
                    if (entry.getValue() == null || entry.getValue().isEmpty()) {
                        log.warn("Empty vector returned for event: {}, skipping", entry.getKey());
                        return false;
                    }
                    return true;
                })
                .map(entry -> {
                    UUID id = (UUID) entry.getKey();
                    Common.PointId pointId = Common.PointId.newBuilder()
                            .setUuid(id.toString())
                            .build();
                    return new QdrantService.UpsertEntry(pointId, entry.getValue(), Collections.emptyMap());
                })
                .collect(Collectors.toList());

        qdrantService.batchUpsert(COLLECTION, entries);
        log.info("Indexed semantic vectors for {}/{} events", entries.size(), toProcess.size());
    }

    /**
     * Find semantically similar events to a given event by UUID.
     * If the event is not yet indexed in Qdrant, it will be embedded and stored first.
     *
     * @param id       The UUID of the reference event.
     * @param pageable Pageable object encoding offset (as page number) and limit (as page size).
     * @return List of UUIDs of similar events, excluding the query event itself.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UUID> findSimilarEvents(UUID id, Pageable pageable) {
        Common.PointId pointId = Common.PointId.newBuilder()
                .setUuid(id.toString())
                .build();

        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        int fetchLimit = offset == 0 ? limit + 1 : offset + limit;

        List<Points.ScoredPoint> searchResults;

        if (!qdrantService.isPointExists(COLLECTION, pointId)) {
            log.info("Event {} not found in Qdrant, embedding and indexing before search.", id);

            EventResponse event = eventService.findById(id);
            String passage = createPassage(event);
            List<Float> queryVector = embeddingService.embed(passage, EmbeddingService.MODE.TEXT);

            if (queryVector.isEmpty()) {
                log.warn("Embedding returned empty vector for event: {}", id);
                return List.of();
            }

            qdrantService.upsert(COLLECTION, pointId, queryVector, null);

            // Use the vector directly for the first search to ensure zero-latency availability
            searchResults = qdrantService.search(COLLECTION, queryVector, SIMILARITY_THRESHOLD, fetchLimit);
        } else {
            // Point exists, use the ID-based search (server-side vector lookup)
            searchResults = qdrantService.search(COLLECTION, pointId, SIMILARITY_THRESHOLD, fetchLimit);
        }

        return searchResults.stream()
                .map(p -> UUID.fromString(p.getId().getUuid()))
                .filter(resultId -> !resultId.equals(id))
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) { return Collections.emptyList(); }

        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        int fetchLimit = offset + limit;

        List<Float> queryVector = embeddingService.embed(query, EmbeddingService.MODE.QUERY);

        if (queryVector.isEmpty()) {
            log.warn("Search cancelled: Could not generate embedding for query: '{}'", query);
            return Collections.emptyList();
        }

        try {
            List<Points.ScoredPoint> scoredPoints = qdrantService.search(
                    COLLECTION,
                    queryVector,
                    SIMILARITY_THRESHOLD,
                    fetchLimit
            );

            return scoredPoints.stream()
                    .map(point -> point.getId().getUuid())
                    .map(UUID::fromString)
                    .skip(offset)
                    .limit(limit)
                    .toList();

        } catch (Exception e) {
            log.error("Semantic search failed for query: '{}'. Error: {}", query, e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public float getSimilarityScore(UUID originalId, UUID targetId) {
        try {
            Common.PointId originPoint = Common.PointId.newBuilder().setUuid(originalId.toString()).build();

            List<Points.ScoredPoint> results = qdrantService.search(COLLECTION, originPoint, 0.0f, 100);

            return (float) results.stream()
                    .filter(p -> p.getId().getUuid().equals(targetId.toString()))
                    .mapToDouble(Points.ScoredPoint::getScore)
                    .findFirst()
                    .orElse(0.1f);
        } catch (Exception e) {
            return 0.1f;
        }
    }
}
