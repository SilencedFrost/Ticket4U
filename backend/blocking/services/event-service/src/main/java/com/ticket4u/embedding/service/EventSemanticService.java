package com.ticket4u.embedding.service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventSemanticService {
    void storeEventVector(UUID id);
    void storeEventVectors(List<UUID> ids, boolean override);
    List<UUID> findSimilarEvents(UUID id, Pageable pageable);
    List<UUID> search(String query, Pageable pageable);
    float getSimilarityScore(UUID originalId, UUID targetId);
}
