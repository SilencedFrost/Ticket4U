package com.ticket4u.feature.homepage.mapper;

import com.ticket4u.feature.homepage.dto.CategoryResponse;
import com.ticket4u.feature.homepage.dto.EventSummaryResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Mapper for converting native query results (Object[]) to DTOs
 * Separated from MapStruct mapper to handle raw SQL results
 */
@Component
public class NativeQueryMapper {

    /**
     * Convert Object[] from native query to EventCardResponse
     * Handles both 7-element array (without category) and 8-element array (with category)
     * 
     * @param row Object array from native query
     *            [0] = event_id (UUID)
     *            [1] = name (String)
     *            [2] = banner_url (String)
     *            [3] = address_line (String)
     *            [4] = start_date (Instant/Timestamp/OffsetDateTime)
     *            [5] = end_date (Instant/Timestamp/OffsetDateTime)
     *            [6] = min_price (Double/BigDecimal)
     *            [7] = category_name (String) - optional
     * @return EventCardResponse for frontend display
     */
    public EventSummaryResponse toEventCardResponse(Object[] row) {
        UUID id = (UUID) row[0];
        String name = (String) row[1];
        String bannerUrl = (String) row[2];
        String addressLine = (String) row[3];
        OffsetDateTime startDate = convertToOffsetDateTime(row[4]);
        OffsetDateTime endDate = convertToOffsetDateTime(row[5]);
        
        BigDecimal minPrice = null;
        if (row[6] != null) {
            if (row[6] instanceof BigDecimal) {
                minPrice = (BigDecimal) row[6];
            } else if (row[6] instanceof Double) {
                minPrice = BigDecimal.valueOf((Double) row[6]);
            }
        }
        
        // row[7] - category_name (optional - only present in filter queries)
        String categoryName = null;
        if (row.length > 7 && row[7] != null) {
            categoryName = (String) row[7];
        }
        
        return new EventSummaryResponse(id, name, bannerUrl, addressLine, startDate, endDate, minPrice, categoryName, null);
    }

    /**
     * Convert Category query result to CategoryResponse
     * 
     * @param row Object array from native query
     *            [0] = category_id (Integer)
     *            [1] = name (String)
     * @return CategoryResponse
     */
    public CategoryResponse toCategoryResponse(Object[] row) {
        Integer id = (Integer) row[0];
        String name = (String) row[1];
        return new CategoryResponse(id, name, null);
    }

    /**
     * Helper method to convert various date/time types to OffsetDateTime
     * PostgreSQL JDBC driver may return Instant, Timestamp, or OffsetDateTime
     * 
     * @param obj Date/time object from database
     * @return OffsetDateTime in UTC
     */
    private OffsetDateTime convertToOffsetDateTime(Object obj) {
        if (obj == null) {
            return null;
        }
        
        if (obj instanceof OffsetDateTime) {
            return (OffsetDateTime) obj;
        } else if (obj instanceof Instant) {
            return ((Instant) obj).atOffset(ZoneOffset.UTC);
        } else if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toInstant().atOffset(ZoneOffset.UTC);
        }
        
        throw new IllegalArgumentException("Unsupported date type: " + obj.getClass());
    }
}
