package com.ev_booking_system.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ev_stations")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class StationModel {

    @Id
    private String id;

    private String name;
    private double lat;
    private double lng;
    private String address;

    private String operatorId;
    private String operatorName;

    private List<String> images = new ArrayList<>();

    private double rating;
    private int reviewCount;

    private List<String> supportsConnectors = new ArrayList<>();

    // Correct for MongoDB
    private List<TariffRuleModel> tariffRules = new ArrayList<>();

    private List<ChargerModel> chargers = new ArrayList<>();

    private String description;
    private String phoneNumber;

    private Map<String, String> operatingHours = new HashMap<>();

    private List<String> amenities = new ArrayList<>();

    private boolean isOpen;

    private Double distance;
}
