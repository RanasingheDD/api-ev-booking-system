package com.ev_booking_system.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;
import com.ev_booking_system.api.service.StationService;

@RestController
@RequestMapping("/api/ev_stations")
public class StationController {

    @Autowired
    private StationRepository stationRepository;


    @Autowired
    private StationService stationService;

    // Add a new EV
    @PostMapping("/add")
    public ResponseEntity<StationModel> addStation(@RequestBody StationModel stationModel) {
        StationModel savedEv = stationRepository.save(stationModel);
        return ResponseEntity.ok(savedEv);
    }

    // Optional: List all EVs
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvs() {
        return ResponseEntity.ok(stationRepository.findAll());
    }

    @GetMapping("/{id}")
    public StationModel getStationById(@PathVariable String id) {
        return stationService.getStationById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStations(@RequestParam("q") String query) {
        List<StationModel> stations = stationService.searchStations(query);
        return ResponseEntity.ok(Map.of("stations", stations));
    }
}
