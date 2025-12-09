package com.ev_booking_system.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;
import com.ev_booking_system.api.service.EvService;

@RestController
@RequestMapping("/api/evs")
public class EvController {

    @Autowired
    private EvService evService;

    @Autowired
    private EvRepository evRepository;

    @PostMapping("/add")
    public ResponseEntity<EvModel> addEv(@RequestBody EvModel ev) {
        evService.addEv(ev);
        return ResponseEntity.ok(ev);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEvs() {
        return ResponseEntity.ok(evRepository.findAll());
    }
}
