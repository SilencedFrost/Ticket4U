package com.ticket4u.crud.repository;

import com.ticket4u.core.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CrudVenueRepository extends JpaRepository<Venue, UUID> {
}