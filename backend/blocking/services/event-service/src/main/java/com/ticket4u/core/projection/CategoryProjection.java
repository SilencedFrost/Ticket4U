package com.ticket4u.core.projection;

/**
 * DB projection for category-only queries returning id and name.
 * Used for filter chips and dropdowns where event data is not needed.
 */
public interface CategoryProjection {
    Integer getId();
    String getName();
}
