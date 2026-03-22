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

    @Value("${ai.embedding.api-url}")
    private String embedApiUrl;

    @Value("${ai.embedding.model-name}")
    private String modelName;

    @Override
    public List<Float> getEmbedding(String text) {
        Map<String, Object> request = Map.of(
                "input", text,
                "model", modelName
        );

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    embedApiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> body = response.getBody();

            if (body != null && body.get("data") instanceof List<?> dataList && !dataList.isEmpty()) {
                if (dataList.getFirst() instanceof Map<?, ?> firstElement) {
                    Object embedding = firstElement.get("embedding");
                    if (embedding instanceof List<?> resList) {
                        return resList.stream()
                                .filter(Number.class::isInstance)
                                .map(n -> ((Number) n).floatValue())
                                .toList();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Embedding failed at {}: {}", embedApiUrl, e.getMessage());
        }
        return Collections.emptyList();
    }
}
