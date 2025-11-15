package com.ev_booking_system.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvDto {
    private String make;
    private String model;
    private String registrationNo;
    private String batteryCapacity;
    private String rangePerCharge;
}
