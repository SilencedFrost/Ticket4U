package com.ticket4u.core.service.impl;

import com.ticket4u.core.exceptions.QdrantOperationException;
import com.ticket4u.core.service.QdrantService;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.ValueFactory;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.qdrant.client.QueryFactory.nearest;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

@Slf4j
@Service
@RequiredArgsConstructor
public class QdrantServiceImpl implements QdrantService {

    @Value("${qdrant.timeout-seconds:5}") // default timeout
    private long timeoutSeconds;

    private final QdrantClient client;

    /**
     * Create a new collection in Qdrant with specific vector settings.
     * @param collectionName Name of the collection to create.
     * @param vectorSize Size of the vector (e.g., 384 or 768).
     * @throws QdrantOperationException if the collection cannot be created.
     */
    @Override
    public void createCollection(String collectionName, int vectorSize) {
        try {
            client.createCollectionAsync(collectionName,
                    Collections.VectorParams.newBuilder()
                            .setSize(vectorSize)
                            .setDistance(Collections.Distance.Cosine)
                            .build()
            ).get(timeoutSeconds, TimeUnit.SECONDS);;
        } catch (Exception e) {
            log.error("Qdrant createCollection failed on collection '{}': {}", collectionName, e.getMessage());
            throw new QdrantOperationException("createCollection", collectionName, "Operation failed");
        }
    }

    /**
     * Check if a collection already exists in the Qdrant database.
     * @param collectionName Name of the collection to check.
     * @return true if exists, false otherwise.
     * @throws QdrantOperationException if there is a connection error.
     */
    @Override
    public boolean isCollectionExists(String collectionName) {
        try {
            return client.listCollectionsAsync().get(timeoutSeconds, TimeUnit.SECONDS).contains(collectionName);
        } catch (Exception e) {
            log.error("Qdrant isCollectionExists failed on collection '{}': {}", collectionName, e.getMessage());
            throw new QdrantOperationException("isCollectionExists", collectionName, "Operation failed");
        }
    }

    /**
     * Insert or update a point (vector and metadata) in a collection.
     * @param collectionName Target collection name.
     * @param id Unique ID of the point.
     * @param vector List of float values representing the vector.
     * @param payload Map of metadata to store with the vector.
     * @throws QdrantOperationException if the upsert process fails.
     */
    @Override
    public void upsert(String collectionName, Common.PointId id, List<Float> vector, Map<String, Object> payload) {
        try {
            Points.PointStruct point = Points.PointStruct.newBuilder()
                    .setId(id)
                    .setVectors(vectors(vector))
                    .putAllPayload(buildPayload(payload))
                    .build();

            Points.UpsertPoints request = Points.UpsertPoints.newBuilder()
                    .setCollectionName(collectionName)
                    .addAllPoints(List.of(point))
                    .setWait(true)
                    .setUpdateMode(Points.UpdateMode.Upsert)   // explicit default, swap to InsertOnly if needed
                    .build();

            client.upsertAsync(request).get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Qdrant upsert failed on collection '{}': {}", collectionName, e.getMessage());
            throw new QdrantOperationException("upsert", collectionName, "Operation failed");
        }
    }

    /**
     * Search for the most similar vectors in a collection.
     * @param collectionName Collection to search in.
     * @param queryVector The vector used for searching.
     * @param threshold Minimum similarity score to include a result.
     * @param limit Maximum number of results to return.
     * @return List of scored points found.
     * @throws QdrantOperationException if the search fails.
     */
    @Override
    public List<Points.ScoredPoint> search(String collectionName, List<Float> queryVector, float threshold, int limit) {
        try {
            Points.QueryPoints queryPoints = Points.QueryPoints.newBuilder()
                    .setCollectionName(collectionName)
                    .setQuery(nearest(queryVector))
                    .setScoreThreshold(threshold)
                    .setLimit(limit)
                    .setWithPayload(enable(true))
                    .build();

            return client.queryAsync(queryPoints).get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Qdrant search failed on collection {}: {}", collectionName, e.getMessage());
            throw new QdrantOperationException("search", collectionName, "Search operation failed");
        }
    }

    /**
     * Retrieve a specific point's data using its ID.
     * @param collectionName Collection to look in.
     * @param id The ID of the point to retrieve.
     * @return The retrieved point data.
     * @throws QdrantOperationException if the ID is not found or connection fails.
     */
    @Override
    public Points.RetrievedPoint getById(String collectionName, Common.PointId id) {
        List<Points.RetrievedPoint> results;
        try {
            results = client.retrieveAsync(collectionName, List.of(id), true, false, null)
                    .get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (QdrantOperationException e) {
            throw e; // rethrow cleanly if already wrapped
        } catch (Exception e) {
            log.error("Qdrant getById failed on collection '{}': {}", collectionName, e.getMessage());
            throw new QdrantOperationException("getById", collectionName, "Retrieve operation failed");
        }

        return results.stream()
                .findFirst()
                .orElseThrow(() -> new QdrantOperationException("Point ID not found in collection '" + collectionName + "': " + id));
    }

    private Map<String, JsonWithInt.Value> buildPayload(Map<String, Object> metadata) {
        return metadata.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> toQdrantValue(e.getValue())
                ));
    }

    private JsonWithInt.Value toQdrantValue(Object val) {
        if (val == null) {
            // NullValue: proto oneof null_value
            return JsonWithInt.Value.newBuilder()
                    .setNullValue(JsonWithInt.NullValue.NULL_VALUE)
                    .build();
        }
        return switch (val) {
            // --- integer family ---
            case Integer i  -> ValueFactory.value(i);
            case Long l     -> ValueFactory.value(l);
            case Short s    -> ValueFactory.value((int) s);
            case Byte b     -> ValueFactory.value((int) b);

            // --- float family ---
            case Double d   -> ValueFactory.value(d);
            case Float f    -> ValueFactory.value((double) f);

            // --- boolean (must be before Number to avoid autoboxing ambiguity) ---
            case Boolean b  -> ValueFactory.value(b);

            // --- string ---
            case String s   -> ValueFactory.value(s);

            // --- list / array (recursive) ---
            case List<?> list -> JsonWithInt.Value.newBuilder()
                    .setListValue(
                            JsonWithInt.ListValue.newBuilder()
                                    .addAllValues(
                                            list.stream()
                                                    .map(this::toQdrantValue)
                                                    .collect(Collectors.toList())
                                    )
                                    .build()
                    )
                    .build();

            // --- nested object / struct (recursive) ---
            case Map<?, ?> map -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> strMap = (Map<String, Object>) map;
                yield JsonWithInt.Value.newBuilder()
                        .setStructValue(
                                JsonWithInt.Struct.newBuilder()
                                        .putAllFields(
                                                strMap.entrySet().stream()
                                                        .collect(Collectors.toMap(
                                                                Map.Entry::getKey,
                                                                e -> toQdrantValue(e.getValue())
                                                        ))
                                        )
                                        .build()
                        )
                        .build();
            }

            // --- safe fallback ---
            default -> ValueFactory.value(val.toString());
        };
    }

    @Override
    public void createCollectionIfAbsent(String collectionName, int vectorSize) {
        try {
            createCollection(collectionName, vectorSize);
        } catch (Exception e) {
            if (e.getCause() instanceof java.util.concurrent.ExecutionException ex
                    && ex.getCause() instanceof io.grpc.StatusRuntimeException srex
                    && srex.getStatus().getCode() == io.grpc.Status.Code.ALREADY_EXISTS) {
                log.info("Collection '{}' already exists, skipping creation.", collectionName);
                return;
            }
            throw new QdrantOperationException("createCollectionIfAbsent", collectionName, "Initialization failed");
        }
    }
}
