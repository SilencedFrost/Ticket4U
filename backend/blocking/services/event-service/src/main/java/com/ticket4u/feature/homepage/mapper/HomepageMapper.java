package com.ticket4u.feature.homepage.mapper;

import com.ticket4u.core.projection.CategoryProjection;
import com.ticket4u.feature.homepage.dto.CategoryResponse;
import org.mapstruct.Mapper;

/**
 * Homepage-specific mapper. Handles only DTOs that belong exclusively to this feature.
 *
 * EventSummaryResponse mapping was intentionally removed from here and moved to
 * core.mapper.EventSummaryMapper, because EventSummaryResponse is shared with
 * feature.eventdetail — a DTO shared by multiple features belongs in core.
 */
@Mapper(componentModel = "spring")
public interface HomepageMapper {

    /** Maps a category projection to the homepage category filter chip response. */
    CategoryResponse toCategoryResponse(CategoryProjection projection);
}
