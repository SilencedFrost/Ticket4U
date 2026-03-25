package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "zoneId", source = "zone.id")
    @Mapping(target = "status", expression = "java(seat.getStatus().name())")
    SeatResponse toDTO(Seat seat);
}