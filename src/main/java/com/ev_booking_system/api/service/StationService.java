package com.ev_booking_system.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    // Other service methods...
    public StationModel getStationById(String id) {
        return stationRepository.findById(id).orElseThrow(()-> new RuntimeException("Station not found"));
    }

    public StationModel addStation(StationModel station) {
        return stationRepository.save(station);
    }

    public List<StationModel> getAll(){
        return stationRepository.findAll();
    }
}
