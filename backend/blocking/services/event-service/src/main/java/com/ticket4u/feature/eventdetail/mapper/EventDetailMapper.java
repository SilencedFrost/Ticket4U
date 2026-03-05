package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventDetailMapper {

    @Mapping(source = "event.id", target = "id")
    @Mapping(source = "event.name", target = "name")
    @Mapping(source = "event.startDate", target = "startDate")
    @Mapping(source = "event.addressLine", target = "addressLine")
    @Mapping(source = "event.description", target = "description")
    @Mapping(source = "event.bannerUrl", target = "bannerUrl")
    @Mapping(source = "event.seatingPlanImageUrl", target = "seatingPlanImageUrl")
    @Mapping(source = "event.category.id", target = "categoryId")
    @Mapping(source = "event.organizerId", target = "organizerId")
    @Mapping(source = "minPrice", target = "minPrice")
    @Mapping(source = "maxPrice", target = "maxPrice")
    @Mapping(source = "showtimes", target = "showtimes")
    EventDetailResponse toResponse(
            Event event,
            java.math.BigDecimal minPrice,
            java.math.BigDecimal maxPrice,
            List<ShowtimeResponse> showtimes);
}
