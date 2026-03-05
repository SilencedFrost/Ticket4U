package com.ticket4u.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.List;
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

    @Column(nullable = false, length = 254)
    private String name;

    @Column(nullable = false, name = "organizer_id")
    private UUID organizerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventStatus status;

    @Column(name = "banner_url", columnDefinition = "TEXT")
    private String bannerUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    @Column(name = "cancelled_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime cancelledAt;

    // Event content fields (gộp từ EventContent)
    @Column(name = "about_vi", columnDefinition = "TEXT")
    private String aboutVi;

    @Column(name = "about_en", columnDefinition = "TEXT")
    private String aboutEn;

    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(name = "policy_refund", columnDefinition = "TEXT")
    private String policyRefund;

    @Column(name = "seating_plan_image_url", columnDefinition = "TEXT")
    private String seatingPlanImageUrl;

    // Relationships
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Zone> zones;

    public enum EventStatus {
        PLANNED,
        ONGOING,
        FINISHED,
        CANCELLED
    }
}
