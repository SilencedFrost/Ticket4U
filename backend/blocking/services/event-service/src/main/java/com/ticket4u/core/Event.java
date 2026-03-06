package com.ticket4u.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(nullable = false, name = "organizer_id")
    private UUID organizerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventStatus status;

    @Column(name = "banner_url", nullable = false, columnDefinition = "TEXT")
    private String bannerUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    @Column(name = "cancelled_at", nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime cancelledAt;

    @Column(name = "about_vi", nullable = true, columnDefinition = "TEXT")
    private String aboutVi;

    @Column(name = "about_en", nullable = true, columnDefinition = "TEXT")
    private String aboutEn;

    @Column(name = "terms_and_conditions", nullable = true, columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(name = "policy_refund", nullable = true, columnDefinition = "TEXT")
    private String policyRefund;

    @Column(name = "seating_plan_image_url", nullable = true, columnDefinition = "TEXT")
    private String seatingPlanImageUrl;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EventSession> sessions = new LinkedHashSet<>();

    public enum EventStatus {
        PLANNED,
        ONGOING,
        FINISHED,
        CANCELLED
    }
}
