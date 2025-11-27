package com.ev_booking_system.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    public Optional<StationModel> getStationById(String id) {
        return stationRepository.findById(id);
    }
}
