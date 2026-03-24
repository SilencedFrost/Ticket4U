package com.ticket4u.embedding.service;

import com.ticket4u.embedding.service.impl.QdrantServiceImpl;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.Points;

import java.util.List;
import java.util.Map;

public interface QdrantService {
    void createCollection(String collectionName, int vectorSize);
    boolean isCollectionExists(String collectionName);
    void upsert(String collectionName, Common.PointId id, List<Float> vector, Map<String, Object> payload);
    void batchUpsert(String collectionName, List<QdrantServiceImpl.UpsertEntry> points);
    List<Points.ScoredPoint> search(String collectionName, List<Float> queryVector, float threshold, int limit);
    Points.RetrievedPoint getById(String collectionName, Common.PointId id);
    Points.RetrievedPoint getById(String collectionName, Common.PointId id, boolean withVectors);
    void createCollectionIfAbsent(String collectionName, int vectorSize);
    List<Common.PointId> filterExistingIds(String collectionName, List<Common.PointId> ids);
    boolean isPointExists(String collectionName, Common.PointId id);
    List<Points.ScoredPoint> search(String collectionName, Common.PointId id, float threshold, int limit);

    public record UpsertEntry(
            Common.PointId id,
            List<Float> vector,
            Map<String, Object> payload
    ) {}
}
