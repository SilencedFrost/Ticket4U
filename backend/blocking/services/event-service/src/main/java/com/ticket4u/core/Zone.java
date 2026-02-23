package com.ticket4u.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "zones", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Zone {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(length = 254)
    private String name;

    @Column(name = "is_standing", nullable = false)
    private Boolean isStanding = false;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold = 0;

    @Column(name = "purchase_limit")
    private Integer purchaseLimit;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @OneToOne(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ZoneContent content;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    private List<Seat> seats;
} 
