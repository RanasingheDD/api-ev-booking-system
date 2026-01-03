package com.ev_booking_system.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;
import com.ev_booking_system.api.service.EvService;



@RestController
@RequestMapping("/api/evs")
@CrossOrigin(origins = "*")
public class EvController {

    @Autowired
    private EvService evService;

    @Autowired
    private EvRepository evRepository;

    // Optional: List all EVs
    @GetMapping("/all")
    public ResponseEntity<List<EvModel>> getAllEvs() {
        return ResponseEntity.ok(evRepository.findAll());
    }
}

