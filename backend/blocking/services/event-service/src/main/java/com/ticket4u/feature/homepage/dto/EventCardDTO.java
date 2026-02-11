package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCardDTO {
    private UUID id;
    private String name;
    private String bannerUrl;
    private String addressLine;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private BigDecimal minPrice;
    private String categoryName;
    private String status;

    // Constructor để map từ Object[] result (native query)
    // categoryName và status sẽ null, cần set riêng nếu cần
    public EventCardDTO(Object[] result) {
        this.id = (UUID) result[0];
        this.name = (String) result[1];
        this.bannerUrl = (String) result[2];
        this.addressLine = (String) result[3];
        // Convert Instant to OffsetDateTime
        this.startDate = result[4] != null ? ((Instant) result[4]).atOffset(ZoneOffset.UTC) : null;
        this.endDate = result[5] != null ? ((Instant) result[5]).atOffset(ZoneOffset.UTC) : null;
        Object priceObj = result[6];
        if (priceObj != null) {
            // Chuyển đổi an toàn từ Object (của Native Query) sang BigDecimal
            this.minPrice = new BigDecimal(priceObj.toString());
        } else {
            this.minPrice = null;
        }
    }
}

