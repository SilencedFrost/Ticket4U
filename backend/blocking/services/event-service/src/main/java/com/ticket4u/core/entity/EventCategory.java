package com.ticket4u.core.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "event_categories", schema = "public")
public class EventCategory {

    @EmbeddedId
    private EventCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Getter
    @Setter
    @EqualsAndHashCode
    @Embeddable
    public static class EventCategoryId implements Serializable {

        @Column(name = "event_id")
        private UUID eventId;

        @Column(name = "category_id")
        private Integer categoryId;
    }
}
