package com.ticket4u.mapper;

import com.ticket4u.dto.OrderResponse;
import com.ticket4u.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = TicketMapper.class
)
public abstract class OrderMapper {
    @Mapping(target = "ticketSummaries", source = "tickets")
    public abstract OrderResponse toDTO(Order order);
}
