package com.ticket4u.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ticket", schema = "public")
@NoArgsConstructor
public class Ticket {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private UUID seatId;

    @Column(nullable = false, length = 32)
    private String seatName;

    @Column(nullable = false)
    private UUID zoneId;

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false, length = 32)
    private String ticketType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime usedAt;

    @Column(nullable = false, length = 64)
    private String qrSecret;
}