package com.ticket4u.core.service;

import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.Points;

import java.util.List;
import java.util.Map;

public interface QdrantService {
    void createCollection(String collectionName, int vectorSize);
    boolean isCollectionExists(String collectionName);
    void upsert(String collectionName, Common.PointId id, List<Float> vector, Map<String, Object> payload);
    List<Points.ScoredPoint> search(String collectionName, List<Float> queryVector, float threshold, int limit);
    Points.RetrievedPoint getById(String collectionName, Common.PointId id);
    void createCollectionIfAbsent(String collectionName, int vectorSize);
}
