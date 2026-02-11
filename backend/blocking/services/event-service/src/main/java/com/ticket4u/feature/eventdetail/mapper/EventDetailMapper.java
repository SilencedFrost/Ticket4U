package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.SeatTypeDTO;
import com.ticket4u.feature.eventdetail.dto.ShowtimeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public interface EvenDetailMapper {

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "name", target = "eventTitle")
    @Mapping(source = "startDate", target = "date", qualifiedByName = "toLocalDateString")
    @Mapping(source = "startDate", target = "time", qualifiedByName = "toLocalTimeString")
    @Mapping(source = "addressLine", target = "address")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "bannerUrl", target = "imgEvent.heroUrl")
    @Mapping(source = "content.seatingPlanImageUrl", target = "imgEvent.seatMapUrl")
    @Mapping(source = "zones", target = "showtimes", qualifiedByName = "mapZonesToShowtimes")
    EventDetailResponse toResponse(Event event);

    @Named("toLocalDateString")
    default String toLocalDateString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Named("toLocalTimeString")
    default String toLocalTimeString(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Named("mapZonesToShowtimes")
    default List<ShowtimeDTO> mapZonesToShowtimes(List<Zone> zones) {
        if (zones == null || zones.isEmpty()) return Collections.emptyList();

        ZoneMapper zoneMapper = org.mapstruct.factory.Mappers.getMapper(ZoneMapper.class);
        List<SeatTypeDTO> seatTypes = zoneMapper.toSeatTypeDTOs(zones);

        return List.of(new ShowtimeDTO(
                zones.get(0).getEvent().getId().toString(),
                toLocalDateString(zones.get(0).getEvent().getStartDate()),
                toLocalTimeString(zones.get(0).getEvent().getStartDate()),
                seatTypes
        ));
    }
}
