package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.projection.EventSummaryProjection;
import com.ticket4u.core.projection.EventWithCategoryProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * MapStruct mapper from core projections → core EventSummaryResponse DTO.
 *
 * Moved to core (extracted from HomepageMapper) because:
 *   - Input  : core.projection  (EventSummaryProjection, EventWithCategoryProjection)
 *   - Output : core.dto         (EventSummaryResponse)
 *
 * A mapper whose input and output are both in core must not live inside a feature.
 * Keeping it in feature.homepage would force feature.eventdetail to import across
 * feature boundaries, violating feature isolation.
 */
@Mapper(componentModel = "spring")
public interface EventSummaryMapper {

    /** Maps a projection without category (categoryName will be null). */
    @Mapping(target = "categoryName", ignore = true)
    EventSummaryResponse toEventSummaryResponse(EventSummaryProjection projection);

    /** Maps a projection that includes categoryName from a JOIN with categories. */
    EventSummaryResponse toEventSummaryResponse(EventWithCategoryProjection projection);

    /**
     * MapStruct auto-discovers this default method for every Instant → OffsetDateTime
     * conversion in this mapper.
     * PostgreSQL JDBC driver returns TIMESTAMPTZ columns as Instant; the API exposes OffsetDateTime.
     */
    default OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
