package com.ticket4u.core.projection;

/**
 * DB projection that extends EventSummaryProjection with the event's category name.
 * Used by queries that JOIN categories — filter/display queries and related-events queries.
 */
public interface EventWithCategoryProjection extends EventSummaryProjection {
    String getCategoryName();
}
