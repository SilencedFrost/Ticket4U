package com.ticket4u.core.service;

import java.util.List;

public interface EmbeddingService {
    List<Float> getDocumentEmbedding(String text);
    List<Float> getQueryEmbedding(String query);
}
