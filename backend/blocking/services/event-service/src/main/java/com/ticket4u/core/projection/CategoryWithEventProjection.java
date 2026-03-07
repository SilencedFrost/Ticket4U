package com.ticket4u.core.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DB projection for flat (category + event) rows returned by the "browse by category" query.
 * Each row represents one event; the service layer groups rows by categoryId into a map.
 * Uses Instant because the PostgreSQL JDBC driver maps TIMESTAMPTZ columns to Instant.
 */
public interface CategoryWithEventProjection {
    Integer getCategoryId();
    String getCategoryName();
    UUID getEventId();
    String getEventName();
    String getBannerUrl();
    String getAddressLine();
    Instant getStartDate();
    Instant getEndDate();
    BigDecimal getMinPrice();
}
