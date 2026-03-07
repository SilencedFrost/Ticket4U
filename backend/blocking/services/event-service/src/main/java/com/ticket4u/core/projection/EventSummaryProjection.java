package com.ticket4u.core.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DB projection for event list queries.
 * Returns: id, name, banner_url, address_line, earliest start_date, latest end_date, cheapest zone price.
 * Uses Instant because the PostgreSQL JDBC driver maps TIMESTAMPTZ columns to Instant.
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
