package com.ticket4u.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "seats", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = true, length = 255)
    private String name;

    @Column(name = "row_name", nullable = true, length = 5)
    private String rowName;

    @Column(name = "col_name", nullable = true, length = 5)
    private String colName;

    @Column(name = "seat_code", nullable = true, length = 20)
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SeatStatus status;

    @Column(name = "price_override", precision = 10, scale = 2)
    private BigDecimal priceOverride;

    public enum SeatStatus {
        AVAILABLE,
        BOOKED,
        HOLD
    }

}
