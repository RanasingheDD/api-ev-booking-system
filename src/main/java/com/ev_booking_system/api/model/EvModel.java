package com.ev_booking_system.api.model;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "evs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvModel {
  @Id  
  private String id;
  private String userId;
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
