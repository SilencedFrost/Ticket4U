package com.ticket4u.core.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface EventCoordinateProjection {
    UUID getId();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
}
