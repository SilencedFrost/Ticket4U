package com.ticket4u.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "event_contents", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class EventContent {

    @Id
    @Column(name = "event_id", updatable = false, nullable = false)
    private UUID eventId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore // ngắt lặp khi trả về json
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Event event;

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
}
