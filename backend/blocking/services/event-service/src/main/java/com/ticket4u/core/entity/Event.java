package com.ticket4u.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Event.withAllEntities",
                attributeNodes = {
                        @NamedAttributeNode("sessions"),
                        @NamedAttributeNode("categories"),
                        @NamedAttributeNode("venue")
                }
        )
})
public class Event {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
    // TODO: implement vector embedding of name

    @Column(nullable = false)
    private UUID organizerId;

    @ManyToMany
    @JoinTable(
            name = "event_categories",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new LinkedHashSet<>();

    @Column(nullable = false)
    private String addressLine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventStatus status;

    @Column(nullable = false, columnDefinition = "text")
    private String bannerUrl;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "timestamptz")
    private OffsetDateTime cancelledAt;

    @Column(columnDefinition = "text")
    private String aboutVi;

    @Column(columnDefinition = "text")
    private String aboutEn;

    @Column(columnDefinition = "text")
    private String termsAndConditions;

    @Column(columnDefinition = "text")
    private String policyRefund;

    @Column(columnDefinition = "text")
    private String seatingPlanImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @JdbcTypeCode(SqlTypes.JSON)
    private String layout;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @OneToMany(mappedBy = "event")
    private Set<EventSession> sessions = new LinkedHashSet<>();

    public enum EventStatus {
        EDITING,
        SCHEDULED,
        PREMIERE,
        SELLING,
        PAUSED,
        ONGOING,
        FINISHED,
        CANCELLED
    }
}
