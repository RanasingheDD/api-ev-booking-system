package com.ev_booking_system.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.model.ChargerModel;
import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    public StationModel getStationById(String id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
    }

    @Cacheable(value = "allStations", key = "'all'")
    public List<StationModel> getAllStations() {
        return stationRepository.findAll();
    }

    public List<StationModel> searchStations(String query) {
        List<StationModel> results = stationRepository.searchByKeyword(query);
        return results.stream()
                .toList();
    }

    public ChargerModel getChargerById(String stationId, String chargerId) {
        // Get station
        StationModel station = this.getStationById(stationId);

        // Find matching charger
        return station.getChargers().stream()
                .filter(ch -> ch.getId().equals(chargerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Charger not found"));
    }

}
