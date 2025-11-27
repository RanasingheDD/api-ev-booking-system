package com.ev_booking_system.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;

@RestController
@RequestMapping("/api/ev_stations")
public class StationController {

    @Autowired
    private StationRepository stationRepository;

    // Add a new EV
    @PostMapping("/add")
    public ResponseEntity<StationModel> addEv(@RequestBody StationModel stationModel) {
        StationModel savedEv = stationRepository.save(stationModel);
        return ResponseEntity.ok(savedEv);
    }

    // Optional: List all EVs
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvs() {
        return ResponseEntity.ok(stationRepository.findAll());
    }

    @GetMapping("/{id}")
    public Optional<ResponseEntity<StationModel>> getStationById(@PathVariable String id) {
        return stationRepository.findById(id)
                .map(station -> ResponseEntity.ok(station));
    }
}
