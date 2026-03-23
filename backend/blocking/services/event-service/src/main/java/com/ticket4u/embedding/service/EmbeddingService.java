package com.ticket4u.embedding.service;

import java.util.List;
import java.util.Map;

public interface EmbeddingService {
    List<Float> embed(String input, String mode);
    Map<Object, List<Float>> embed(Map<Object, String> inputs, String mode);

    class MODE {
        public static final String TEXT = "text";
        public static final String QUERY = "query";
    }
}
