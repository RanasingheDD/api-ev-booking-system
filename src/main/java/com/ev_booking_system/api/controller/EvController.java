package com.ev_booking_system.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;

@RestController
@RequestMapping("/api/evs")
public class EvController {

    @Autowired
    private EvRepository evRepository;

    // Add a new EV
    @PostMapping("/add")
    public ResponseEntity<EvModel> addEv(@RequestBody EvModel ev) {
        EvModel savedEv = evRepository.save(ev);
        return ResponseEntity.ok(savedEv);
    }

    // Optional: List all EVs
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvs() {
        return ResponseEntity.ok(evRepository.findAll());
    }
}
