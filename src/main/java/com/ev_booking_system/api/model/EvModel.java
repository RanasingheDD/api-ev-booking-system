package com.ev_booking_system.api.model;
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

    private String model;
    private String registrationNo;
    private String batteryCapacity;
    private String speed;
    private String ownerId; // Reference to User (the EV owner)
}
