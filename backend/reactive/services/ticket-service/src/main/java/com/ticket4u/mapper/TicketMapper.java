package com.ticket4u.mapper;

import com.ticket4u.dto.TicketSummaryResponse;
import com.ticket4u.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TicketMapper {
    public abstract TicketSummaryResponse toSummaryDTO(Ticket ticket);
}
