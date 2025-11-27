package com.ev_booking_system.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ev_stations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationModel {

    private String id;
    private String owner_name;
    private long mobile;
    private String username;
    private String password;
}
