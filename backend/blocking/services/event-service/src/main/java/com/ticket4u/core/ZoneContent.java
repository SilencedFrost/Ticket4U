package com.ticket4u.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "zone_contents", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class ZoneContent {

    @Id
    @Column(name = "zone_id", updatable = false, nullable = false)
    private UUID zone_id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "zone_id", nullable = false)
    @JsonIgnore // ngắt lặp khi trả về json
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Zone zone;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "gift_image_url", length = 512)
    private String giftImageUrl;

    @Column(columnDefinition = "JSONB")
    private String perks;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    public List<String> getPerksAsList() {
        try {
            return new ObjectMapper().readValue(this.perks, new TypeReference<List<String>>(){});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
