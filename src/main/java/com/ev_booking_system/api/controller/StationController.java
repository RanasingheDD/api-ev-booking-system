package com.ev_booking_system.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;

@RestController
@RequestMapping("/api/ev_stations")
public class StationController {

    @Autowired
    private StationRepository StationRepository;

    // Add a new EV
    @PostMapping("/add")
    public ResponseEntity<StationModel> addEv(@RequestBody StationModel stationModel) {
        StationModel savedEv = StationRepository.save(stationModel);
        return ResponseEntity.ok(savedEv);
    }

    // Optional: List all EVs
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvs() {
        return ResponseEntity.ok(StationRepository.findAll());
    }
}
