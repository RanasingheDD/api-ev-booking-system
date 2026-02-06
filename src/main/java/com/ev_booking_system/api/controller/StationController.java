package com.ev_booking_system.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.StationRepository;
import com.ev_booking_system.api.service.StationService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/ev_stations")
public class StationController {

    @Autowired
    private StationRepository stationRepository;


    @Autowired
    private StationService stationService;

    // Add a new EV
    @PreAuthorize("hasAuthority('OWNER')")
    @PostMapping("/add")
    public ResponseEntity<StationModel> addStation(@RequestBody StationModel stationModel) {
        StationModel savedEv = stationRepository.save(stationModel);
        return ResponseEntity.ok(savedEv);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllEvs() {
        return ResponseEntity.ok(stationRepository.findAll());
    }


    @GetMapping("/{id}")
    public StationModel getStationById(@PathVariable String id) {
        return stationService.getStationById(id);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStation(
            @PathVariable String id,
            @RequestBody StationModel stationModel,
            Authentication auth) {

        try {
            // Get existing station
            Optional<StationModel> existingStation = stationRepository.findById(id);

            if (existingStation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Station not found"));
            }

            // Optional: Verify ownership
            // String username = auth.getName();
            // if (!existingStation.get().getOperatorId().equals(username)) {
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN)
            //         .body(Map.of("error", "You don't have permission to update this station"));
            // }

            // Update the station
            stationModel.setId(id);
            StationModel updatedStation = stationRepository.save(stationModel);

            return ResponseEntity.ok(updatedStation);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update station: " + e.getMessage()));
        }
    }

    /**
     * Delete station (NEW ENDPOINT)
     */
    @PreAuthorize("hasAuthority('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStation(
            @PathVariable String id,
            Authentication auth) {

        try {
            // Get existing station
            Optional<StationModel> existingStation = stationRepository.findById(id);

            if (existingStation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Station not found"));
            }

            // Optional: Verify ownership
            // String username = auth.getName();
            // if (!existingStation.get().getOperatorId().equals(username)) {
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN)
            //         .body(Map.of("error", "You don't have permission to delete this station"));
            // }

            // Delete the station
            stationRepository.deleteById(id);

            return ResponseEntity.ok(Map.of(
                    "message", "Station deleted successfully",
                    "id", id
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete station: " + e.getMessage()));
        }
    }

    /**
     * Get stations by owner/operator (NEW ENDPOINT)
     */
    @PreAuthorize("hasAuthority('OWNER')")
    @GetMapping("/owner/{operatorId}")
    public ResponseEntity<?> getStationsByOwner(@PathVariable String operatorId) {
        try {
            List<StationModel> stations = stationRepository.findByOperatorId(operatorId);
            return ResponseEntity.ok(stations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch stations: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStations(@RequestParam("q") String query) {
        List<StationModel> stations = stationService.searchStations(query);
        return ResponseEntity.ok(Map.of("stations", stations));
    }
}
