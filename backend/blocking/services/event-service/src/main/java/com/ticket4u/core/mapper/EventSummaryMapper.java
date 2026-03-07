package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.dto.EventWithCategoryDto;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface EventSummaryMapper {

    EventSummaryResponse toDTO(EventWithCategoryDto dto);

    default OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
