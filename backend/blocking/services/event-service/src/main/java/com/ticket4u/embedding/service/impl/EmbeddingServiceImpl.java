package com.ticket4u.embedding.service.impl;

import com.ticket4u.embedding.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {

    private final RestTemplate restTemplate;

    @Value("${application.services.embedding}")
    private String embedApiUrl;

    @Override
    public List<Float> embed(String input, String mode) {
        if (input == null) return Collections.emptyList();
        List<List<Float>> result = callEmbeddingApi(List.of(input), mode);
        if (result != null && !result.isEmpty()) return result.getFirst();
        return Collections.emptyList();
    }

    /**
     * A wrapper to map the first key of the input to the corresponding embedding list,
     * maintaining the original order of the input map.
     * * @param inputs A Map where keys are identifiers and values are the text inputs.
     * @param mode The model mode.
     * @return An ordered Map linking the original keys to their respective Float vectors.
     */
    @Override
    public Map<Object, List<Float>> embed(Map<Object, String> inputs, String mode) {
        if (inputs == null || inputs.isEmpty()) {
            return Collections.emptyMap();
        }

        // Preserve order of keys
        List<Object> keys = new ArrayList<>(inputs.keySet());

        List<String> flatInputs = keys.stream()
                .map(inputs::get)
                .toList();

        List<List<Float>> allVectors = callEmbeddingApi(flatInputs, mode);

        Map<Object, List<Float>> result = new LinkedHashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            if (i < allVectors.size()) {
                result.put(keys.get(i), allVectors.get(i));
            }
        }

        return result;
    }

    /**
     * Private helper to send requests to the Python embedding service.
     * @param inputs The texts to be processed.
     * @param mode The specific mode (text, query, image, image_text) for the AI model.
     * @return The resulting vector list from the AI service.
     */
    private List<List<Float>> callEmbeddingApi(List<String> inputs, String mode) {
        // Body
        Map<String, Object> request = Map.of(
                "texts", inputs,
                "mode", mode
        );

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    embedApiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> body = response.getBody();

            if (body != null && body.get("embeddings") instanceof List<?> allEmbeds) {
                return allEmbeds.stream()
                        .filter(List.class::isInstance)
                        .map(inner -> ((List<?>) inner).stream()
                                .filter(Number.class::isInstance)
                                .map(n -> ((Number) n).floatValue())
                                .toList())
                        .toList();
            }
        } catch (Exception e) {
            log.error("Embedding Error | Mode: {} | Msg: {}", mode, e.getMessage());
        }
        return Collections.emptyList();
    }
}
