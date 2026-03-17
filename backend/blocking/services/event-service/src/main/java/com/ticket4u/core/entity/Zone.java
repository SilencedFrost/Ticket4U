package com.ticket4u.core.entity;

import com.ticket4u.core.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "zones", schema = "public")
public class Zone {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private EventSession session;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isStanding;

    private Integer capacity;
    private Integer quantitySold;
    private Integer purchaseLimit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description_vi", columnDefinition = "text")
    private String descriptionVi;

    @Column(name = "description_en", columnDefinition = "text")
    private String descriptionEn;

    @Column(length = 512)
    private String giftImageUrl;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> perks;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "zone")
    private Set<Seat> seats = new HashSet<>();
}