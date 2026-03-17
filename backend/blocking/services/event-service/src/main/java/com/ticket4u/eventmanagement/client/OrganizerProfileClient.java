package com.ticket4u.eventmanagement.client;

import com.ticket4u.eventmanagement.dto.OrganizerProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Component
public class OrganizerProfileClient {

    private final RestClient restClient;

    public OrganizerProfileClient(@Qualifier("userServiceClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public OrganizerProfileResponse getOrganizerProfile(UUID organizerId) {
        try {
            return restClient.get()
                    .uri("/api/v1/internal/organizers/{id}", organizerId)
                    .retrieve()
                    .body(OrganizerProfileResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch organizer profile for id={}: {}", organizerId, e.getMessage());
            return new OrganizerProfileResponse(organizerId, "Unknown Organizer", null);
        }
    }
}