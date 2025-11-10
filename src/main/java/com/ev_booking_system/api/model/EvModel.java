package com.ev_booking_system.api.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "evs")
public class EvModel {
    @Id
    private String id;

    private String make;
    private String model;
    private String registrationNumber;
    private String batteryCapacity;
    private String rangePerCharge;

    private String ownerId; // Reference to User (the EV owner)
}
