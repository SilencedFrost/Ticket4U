package com.ticket4u.repository;

import com.ticket4u.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "role")
    Optional<User> findWithRoleByEmailIgnoreCase(String email);
    
    boolean existsByEmailIgnoreCase(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("DELETE FROM User u WHERE u.isActive = false AND u.createdAt < :cutoffDate")
    int deleteInactiveAccounts(@Param("cutoffDate") OffsetDateTime cutoffDate);
}
