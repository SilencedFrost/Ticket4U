package com.ticket4u.feature.homePage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
        this.startDate = (OffsetDateTime) result[4];
        this.endDate = (OffsetDateTime) result[5];
        this.minPrice = (BigDecimal) result[6];
    }
}

