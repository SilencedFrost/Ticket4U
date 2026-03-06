package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.core.EventSession;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public interface EventDetailMapper {

    @Mapping(source = "event.id", target = "id")
    @Mapping(source = "event.name", target = "name")
    @Mapping(source = "event.category.id", target = "categoryId")
    @Mapping(source = "event.organizerId", target = "organizerId")
    @Mapping(source = "event.sessions", target = "showtimes")
    EventDetailResponse toResponse(
            Event event,
            OffsetDateTime startDate,
            java.math.BigDecimal minPrice,
            java.math.BigDecimal maxPrice);

    @Mapping(source = "zones", target = "seatTypes")
    ShowtimeResponse toShowtimeResponse(EventSession session);
}
