package com.ticket4u.eventmanagement.repository;

import com.ticket4u.core.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventManagementVenueRepository extends JpaRepository<Venue, UUID> {
}