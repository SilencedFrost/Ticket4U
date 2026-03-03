package com.ticket4u.feature.ticketselect.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.core.EventLayout;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.ticketselect.dto.FloorDTO;
import com.ticket4u.feature.ticketselect.dto.TicketSelectResponse;
import com.ticket4u.feature.ticketselect.dto.ZoneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketSelectMapper {

    @Mapping(source = "id",           target = "eventId")
    @Mapping(source = "name",         target = "name")
    @Mapping(source = "startDate",    target = "startDate",  qualifiedByName = "toDateTimeString")
    @Mapping(source = "endDate",      target = "endDate",    qualifiedByName = "toDateTimeString")
    @Mapping(source = "addressLine",  target = "addressLine")
    @Mapping(source = "zones",        target = "zones")
    @Mapping(source = "eventLayouts", target = "floors",     qualifiedByName = "toFloorDTOs")
    TicketSelectResponse toResponse(Event event);

    @Mapping(source = "id",            target = "id")
    @Mapping(source = "name",          target = "name")
    @Mapping(source = "price",         target = "price",         qualifiedByName = "toDoublePrice")
    @Mapping(source = "capacity",      target = "capacity")
    @Mapping(source = "quantitySold",  target = "quantitySold")
    @Mapping(source = "purchaseLimit", target = "purchaseLimit")
    @Mapping(source = "color",         target = "color")
    @Mapping(source = "isStanding",    target = "isStanding")
    ZoneDTO toZoneDTO(Zone zone);

    List<ZoneDTO> toZoneDTOs(List<Zone> zones);

    // ── Floor mapping ──────────────────────────────────────
    @Named("toFloorDTOs")
    default List<FloorDTO> toFloorDTOs(List<EventLayout> layouts) {
        if (layouts == null) return Collections.emptyList();
        return layouts.stream()
                .sorted((a, b) -> Integer.compare(a.getFloorOrder(), b.getFloorOrder()))
                .map(this::toFloorDTO)
                .toList();
    }

    default FloorDTO toFloorDTO(EventLayout layout) {
        String layoutJson     = layout.getVenueLayout() != null
                ? layout.getVenueLayout().getLayoutJson()
                : null;
        String modifications  = layout.getModification() != null
                ? layout.getModification().getModificationsJson()
                : null;

        return new FloorDTO(
                layout.getId(),
                layout.getFloorName(),
                layout.getFloorOrder(),
                layoutJson,
                modifications
        );
    }

    // ── Named converters ──────────────────────────────────
    @Named("toDateTimeString")
    default String toDateTimeString(OffsetDateTime dt) {
        return dt == null ? null : dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }

    @Named("toDoublePrice")
    default double toDoublePrice(java.math.BigDecimal price) {
        return price == null ? 0.0 : price.doubleValue();
    }
}