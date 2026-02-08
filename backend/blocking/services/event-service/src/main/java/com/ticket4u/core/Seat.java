package com.ticket4u.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "zones", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false ,nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Column(name = "row_name")
    private String rowName;

    @Column(name = "col_name")
    private String colName;

    @Column(name = "sear_code")
    private String seatCode;

    @Column(name = "status", nullable = false)
    private SeatStatus status;

    public enum SeatStatus {
        AVAILABLE,
        BOOKED,
        HOLD
    }

}
