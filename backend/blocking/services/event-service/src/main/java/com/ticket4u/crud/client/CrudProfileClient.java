package com.ticket4u.crud.client;

import com.ticket4u.crud.dto.CrudProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Component
public class CrudProfileClient {

    private final RestClient restClient;

    public CrudProfileClient(@Qualifier("userServiceClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public CrudProfileResponse getOrganizerProfile(UUID organizerId) {
        try {
            return restClient.get()
                    .uri("/api/v1/internal/organizers/{id}", organizerId)
                    .retrieve()
                    .body(CrudProfileResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch organizer profile for id={}: {}", organizerId, e.getMessage());
            return new CrudProfileResponse(organizerId, "Unknown Organizer", null);
        }
    }
}