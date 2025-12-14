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

    private String make;
    private String model;
    private int year;
    private double maxChargeKw;
    private String vin;
    private String nickname;
    private double mileage;
<<<<<<< Updated upstream
    private String registrationNo;
=======
>>>>>>> Stashed changes

}
