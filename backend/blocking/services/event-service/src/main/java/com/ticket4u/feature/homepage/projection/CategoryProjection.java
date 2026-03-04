package com.ticket4u.feature.homepage.projection;

/**
 * Projection for category-only queries (id + name)
 */
public interface CategoryProjection {
    Integer getId();
    String getName();
}
