package com.ticket4u.repository;

import com.ticket4u.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order>findByIdAndUserId(UUID orderId, UUID userId);
}
