package com.ticket4u.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "event_layouts", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class EventLayout {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private VenueLayout venueLayout;

    @Column(name = "floor_name", nullable = false, length = 255)
    private String floorName;

    @Column(name = "floor_order", nullable = false)
    private Integer floorOrder;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @OneToOne(mappedBy = "eventLayout", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EventLayoutModification modification;
}