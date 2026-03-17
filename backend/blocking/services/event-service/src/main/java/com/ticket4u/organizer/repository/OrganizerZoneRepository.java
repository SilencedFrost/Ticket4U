package com.ticket4u.organizer.repository;

import com.ticket4u.core.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizerZoneRepository extends JpaRepository<Zone, UUID> {

    List<Zone> findAllBySessionId(UUID sessionId);

    Optional<Zone> findByIdAndSessionId(UUID id, UUID sessionId);

    void deleteAllBySessionId(UUID sessionId);
}