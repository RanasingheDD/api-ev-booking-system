package com.ev_booking_system.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvDto {
  private String id;
  private String make;
  private String model;
  private int year;
  private double batteryKwh;
  private double maxChargeKw;
  private List<String> connectorTypes;
  private String vin;
  private String licensePlate;
  private String nickname;
  private LocalDateTime createdAt;
}
