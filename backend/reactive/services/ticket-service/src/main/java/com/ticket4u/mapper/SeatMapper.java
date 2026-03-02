package com.ticket4u.mapper;

import com.ticket4u.dto.SeatResponse;
import com.ticket4u.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse toDTO (Ticket ticket);
}
