package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.VenueSummaryResponse;
import com.ticket4u.core.entity.Venue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface VenueMapper {
    VenueSummaryResponse toSummaryDTO(Venue venue);
}
