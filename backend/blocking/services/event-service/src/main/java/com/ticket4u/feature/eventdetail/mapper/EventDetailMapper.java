package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.SeatTypeResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public interface EventDetailMapper {

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "name", target = "eventTitle")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "addressLine", target = "address")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "bannerUrl", target = "imgEvent.heroUrl")
    @Mapping(source = "seatingPlanImageUrl", target = "imgEvent.seatMapUrl")
    @Mapping(source = "zones", target = "showtimes", qualifiedByName = "mapZonesToShowtimes")
    @Mapping(target = "minPrice", ignore = true)
    @Mapping(target = "maxPrice", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    EventDetailResponse toResponse(Event event, @Context ZoneMapper zoneMapper);

    @Named("mapZonesToShowtimes")
    default List<ShowtimeResponse> mapZonesToShowtimes(List<Zone> zones, @Context ZoneMapper zoneMapper) {
        if (zones == null || zones.isEmpty()) return Collections.emptyList();

        List<SeatTypeResponse> seatTypes = zoneMapper.toSeatTypeResponse(zones);

        Event event = zones.get(0).getEvent();
        return List.of(new ShowtimeResponse(
                event.getId().toString(),
                event.getStartDate(),
                event.getStartDate(),
                seatTypes
        ));
    }
}
