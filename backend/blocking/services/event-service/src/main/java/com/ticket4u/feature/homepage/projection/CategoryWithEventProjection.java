package com.ticket4u.feature.homepage.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Projection for the single categories-with-events query.
 * Each row is a flat (category + event) pair.
 * Uses Instant because PostgreSQL JDBC driver returns Instant for timestamp columns.
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
