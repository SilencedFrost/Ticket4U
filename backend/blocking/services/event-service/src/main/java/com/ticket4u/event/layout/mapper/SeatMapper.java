package com.ticket4u.event.layout.mapper;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse toDTO(Seat seat);
}