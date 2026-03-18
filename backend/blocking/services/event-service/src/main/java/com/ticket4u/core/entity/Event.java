package com.ticket4u.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

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
                        @NamedAttributeNode(value = "sessions", subgraph = "sessions-subgraph"),
                        @NamedAttributeNode("category"),
                        @NamedAttributeNode("venue")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "sessions-subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("zones")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "Event.withSessionsAndZones",
                attributeNodes = {
                        @NamedAttributeNode(
                                value = "sessions",
                                subgraph = "sessions-subgraph"
                        )
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "sessions-subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("zones")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "Event.withSessions",
                attributeNodes = {
                        @NamedAttributeNode("sessions")
                }
        )
})
public class Event {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private UUID organizerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

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

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @OneToMany(mappedBy = "event")
    private Set<EventSession> sessions = new LinkedHashSet<>();

    @Column(name = "layout", columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String layout;

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