package com.ticket4u.organizer.repository;

import com.ticket4u.core.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizerVenueRepository extends JpaRepository<Venue, UUID> {
}