package com.ticket4u.feature.homepage.mapper;

import com.ticket4u.core.Zone;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Helper mapper for HomePage feature
 * Contains custom mapping logic for calculating prices and totals from zones
 */
@Component
@Named("HomePageHelperMapper")
public class HomePageHelperMapper {

    /**
     * Calculate minimum price from list of zones
     * Used for displaying "Từ {minPrice}" on event cards
     */
    @Named("computeMinPrice")
    public BigDecimal computeMinPrice(List<Zone> zones) {
        if (zones == null || zones.isEmpty()) {
            return null;
        }
        Optional<BigDecimal> minPrice = zones.stream()
                .map(Zone::getPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo);
        return minPrice.orElse(null);
    }

    /**
     * Calculate maximum price from list of zones
     */
    @Named("computeMaxPrice")
    public BigDecimal computeMaxPrice(List<Zone> zones) {
        if (zones == null || zones.isEmpty()) {
            return null;
        }
        Optional<BigDecimal> maxPrice = zones.stream()
                .map(Zone::getPrice)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo);
        return maxPrice.orElse(null);
    }

    /**
     * Calculate total capacity from all zones
     */
    @Named("computeTotalCapacity")
    public Integer computeTotalCapacity(List<Zone> zones) {
        if (zones == null || zones.isEmpty()) {
            return 0;
        }
        return zones.stream()
                .map(Zone::getCapacity)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    /**
     * Calculate total sold tickets from all zones
     */
    @Named("computeTotalSold")
    public Integer computeTotalSold(List<Zone> zones) {
        if (zones == null || zones.isEmpty()) {
            return 0;
        }
        return zones.stream()
                .map(Zone::getQuantitySold)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }
}
