package com.ticket4u.feature.homepage.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Projection for event queries returning: id, name, banner_url, address_line, start_date, end_date, min_price.
 * Uses Instant because PostgreSQL JDBC driver returns Instant for timestamp columns.
 */
public interface EventSummaryProjection {
    UUID getId();
    String getName();
    String getBannerUrl();
    String getAddressLine();
    Instant getStartDate();
    Instant getEndDate();
    BigDecimal getMinPrice();
}
