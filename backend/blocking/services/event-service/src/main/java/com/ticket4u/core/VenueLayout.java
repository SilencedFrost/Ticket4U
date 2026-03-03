package com.ticket4u.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "venue_layouts", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class VenueLayout {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    @Column(name = "created_by")
    private UUID createdBy;

    // Use TEXT so Hibernate reads JSONB as plain String — no Jackson needed
    @Column(name = "layout_json", nullable = false, columnDefinition = "TEXT")
    private String layoutJson;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "venueLayout", fetch = FetchType.LAZY)
    private List<EventLayout> eventLayouts;
}