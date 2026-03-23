package com.ticket4u.core.service.impl;

import com.ticket4u.core.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {

    private final RestTemplate restTemplate;

    @Value("${application.services.embedding}")
    private String embedApiUrl;

    /**
     * Converts long text/document into a vector for storage.
     * Uses "text" mode to help the AI understand this is a content to remember.
     * @param text The input content.
     * @return List of float values representing the vector.
     */
    @Override
    public List<Float> getDocumentEmbedding(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        log.info("Generating document embedding for indexing...");
        return callEmbeddingApi(text, "text");
    }

    /**
     * Converts a user search query into a vector for searching.
     * Uses "query" mode to help the AI find the best results.
     * @param query The search text from user.
     * @return List of float values for searching.
     */
    @Override
    public List<Float> getQueryEmbedding(String query) {
        if (query == null || query.isBlank()) return Collections.emptyList();
        log.info("Generating query embedding for indexing...");
        return callEmbeddingApi(query, "query");
    }

    /**
     * Private helper to send requests to the Python AI service.
     * @param input The text to be processed.
     * @param mode The specific mode (text or query) for the AI model.
     * @return The resulting vector list from the AI service.
     */
    private List<Float> callEmbeddingApi(String input, String mode) {
        Map<String, Object> request = Map.of(
                "texts", List.of(input),
                "mode", mode
        );

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    embedApiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> body = response.getBody();

            if (body != null && body.get("embeddings") instanceof List<?> allEmbeds) {
                if (!allEmbeds.isEmpty() && allEmbeds.get(0) instanceof List<?> firstEmbed) {
                    return firstEmbed.stream()
                            .filter(Number.class::isInstance)
                            .map(n -> ((Number) n).floatValue())
                            .toList();
                }
            }
        } catch (Exception e) {
            log.error("Embedding Error | Mode: {} | Msg: {}", mode, e.getMessage());
        }
        return Collections.emptyList();
    }
}
