package com.ticket4u.core.service;

import java.util.List;

public interface EmbeddingService {
    List<Float> getEmbedding(String text);
}
