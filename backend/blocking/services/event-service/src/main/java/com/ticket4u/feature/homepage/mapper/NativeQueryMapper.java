package com.ticket4u.feature.homepage.mapper;

import com.ticket4u.feature.homepage.dto.CategoryResponse;
import com.ticket4u.feature.homepage.dto.EventSummaryResponse;
import com.ticket4u.feature.homepage.projection.CategoryProjection;
import com.ticket4u.feature.homepage.projection.EventSummaryProjection;
import com.ticket4u.feature.homepage.projection.EventWithCategoryProjection;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Mapper for converting JPA Projection results to DTOs.
 * All type safety is guaranteed by the projection interfaces — no manual casting.
 * Handles Instant → OffsetDateTime conversion (PostgreSQL JDBC returns Instant).
 */
@Component
public class NativeQueryMapper {

    /**
     * Convert EventSummaryProjection to EventSummaryResponse
     */
    public EventSummaryResponse toEventSummaryResponse(EventSummaryProjection projection) {
        return new EventSummaryResponse(
                projection.getId(),
                projection.getName(),
                projection.getBannerUrl(),
                projection.getAddressLine(),
                toOffsetDateTime(projection.getStartDate()),
                toOffsetDateTime(projection.getEndDate()),
                projection.getMinPrice(),
                null
        );
    }

    /**
     * Convert EventWithCategoryProjection to EventSummaryResponse (includes category name)
     */
    public EventSummaryResponse toEventSummaryResponse(EventWithCategoryProjection projection) {
        return new EventSummaryResponse(
                projection.getId(),
                projection.getName(),
                projection.getBannerUrl(),
                projection.getAddressLine(),
                toOffsetDateTime(projection.getStartDate()),
                toOffsetDateTime(projection.getEndDate()),
                projection.getMinPrice(),
                projection.getCategoryName()
        );
    }

    /**
     * Convert CategoryProjection to CategoryResponse
     */
    public CategoryResponse toCategoryResponse(CategoryProjection projection) {
        return new CategoryResponse(projection.getId(), projection.getName());
    }

    /**
     * Convert Instant (from PostgreSQL JDBC) to OffsetDateTime (UTC)
     */
    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
