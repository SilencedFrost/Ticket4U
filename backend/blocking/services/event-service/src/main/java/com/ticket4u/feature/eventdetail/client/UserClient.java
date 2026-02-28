package com.ticket4u.feature.eventdetail.client;

import com.ticket4u.feature.eventdetail.dto.OrganizerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestClient userServiceClient;

    public OrganizerResponse getOrganizerById(UUID id) {
        return userServiceClient.get()
                .uri("/api/v1/public/organizers/{id}", id)
                .retrieve()
                .body(OrganizerResponse.class);
    }
}
