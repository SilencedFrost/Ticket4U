package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.SeatTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    List<SeatTypeResponse> toSeatTypeResponse(List<Zone> zones);

    @Mapping(source = "price", target = "price", qualifiedByName = "toStringPrice")
    @Mapping(target = "available", expression = "java(zone.getCapacity() - zone.getQuantitySold())")
    @Mapping(source = "content.description", target = "description")
    @Mapping(source = "content.giftImageUrl", target = "image")
    @Mapping(target = "benefits", expression = "java(zone.getContent() != null ? zone.getContent().getPerksAsList() : java.util.Collections.emptyList())")
    SeatTypeResponse toSeatTypeResponse(Zone zone);

    @Named("toStringPrice")
    default String toStringPrice(java.math.BigDecimal price) {
        return com.ticket4u.utils.PriceFormatter.format(price);
    }
}
