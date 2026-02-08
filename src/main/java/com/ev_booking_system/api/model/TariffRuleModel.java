package com.ev_booking_system.api.model;

//import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TariffRuleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    // @Enumerated(EnumType.STRING)
    private TariffType type;

    private double price;
    private Double flatFee;
    private String currency;
    private String description;
    private String connectorType;

    private Double minPowerKw;
    private Double maxPowerKw;

    // @Embedded
    private TimeRange peakHours;

    private Double peakMultiplier;

    // ============================================
    // ENUM: TariffType
    // ============================================
    public enum TariffType {
        PER_KWH,
        PER_MINUTE,
        FLAT_FEE,
        FLAT_PLUS_KWH
    }

    // ============================================
    // EMBEDDABLE: TimeRange
    // ============================================
    // @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRange implements Serializable {
        private static final long serialVersionUID = 1L;
        private int startHour;
        private int endHour;
    }
}
