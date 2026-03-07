package com.ticket4u.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "seats", schema = "public")
public class Seat {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    private String name;

    @Column(length = 5)
    private String rowName;

    @Column(length = 5)
    private String colName;

    @Column(length = 20)
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SeatStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceOverride;

    public enum SeatStatus {
        AVAILABLE,
        BOOKED,
        HOLD
    }
}
