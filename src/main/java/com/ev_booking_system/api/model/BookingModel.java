package com.ev_booking_system.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

import java.io.Serializable;

@Document(collection = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String userId;
    private String chargerId;
    private String stationId;

    private Instant startAt;
    private Instant endAt;

    private BookingStatus status;

    private String paymentId;
    private Double estimatedEnergy;
    private Double estimatedCost;
    private Double finalCost;

    private String qrCode;
    private String evId;
    private Integer pointsToDeduct;

    private Instant createdAt;

    private StationModel station;
    private ChargerModel charger;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        ACTIVE,
        COMPLETED,
        CANCELLED,
        EXPIRED,
        NO_SHOW
    }

}
