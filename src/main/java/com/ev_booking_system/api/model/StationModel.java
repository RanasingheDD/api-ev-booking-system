package com.ev_booking_system.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ev_stations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationModel {

    @Id
    private String id;

    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String location;
    private String owner_name;
    private long mobile;
    private List<String> chargers;
}
