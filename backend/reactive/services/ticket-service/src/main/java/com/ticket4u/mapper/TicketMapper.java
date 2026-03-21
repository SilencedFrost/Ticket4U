package com.ticket4u.mapper;

import com.ticket4u.dto.TicketResponse;
import com.ticket4u.dto.TicketSummaryResponse;
import com.ticket4u.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketSummaryResponse toSummaryDTO(Ticket ticket);

    @Mapping(target = "orderId", source = "order.id")
    TicketResponse toDTO(Ticket ticket);
}
