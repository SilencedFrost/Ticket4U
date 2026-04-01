package com.ticket4u.mapper;

import com.ticket4u.dto.session.SessionResponse;
import com.ticket4u.entity.Session;
import com.ticket4u.util.SessionDisplayId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = SessionDisplayId.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface SessionMapper {

    @Mapping(target = "displayId", source = "id", qualifiedByName = "toDisplayId")
    SessionResponse toResponse(Session session);

    List<SessionResponse> toResponseList(List<Session> sessions);
}
