package com.ticket4u.feature.homepage.projection;

/**
 * Projection for filter queries returning event data + category_name
 */
public interface EventWithCategoryProjection extends EventSummaryProjection {
    String getCategoryName();
}
