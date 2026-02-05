package com.ev_booking_system.api.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingQuoteDto {
    
  private String chargerId;
  private String stationId;
  private Instant startAt;
  private Instant endAt;
  private double estimatedEnergy;
  private double estimatedCost;
  private String currency;
  private boolean available;
  private String unavailableReason;
}
