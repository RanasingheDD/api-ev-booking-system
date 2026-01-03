package com.ev_booking_system.api.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;
import com.ev_booking_system.api.service.EVService;
import com.ev_booking_system.api.model.EvModel;


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

    // ✅ FETCH EVs OF LOGGED-IN USER
    @GetMapping("/my")
    public ResponseEntity<List<EvModel>> getMyEvs(Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                evRepository.findByUserEmail(principal.getName())
        );
    }
  
    @Autowired
    private EVService evService;

    // ✅ ADD EV (save ONCE)
    @PostMapping("/add")
    public ResponseEntity<EvModel> addEv(@RequestBody EvModel ev, Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        ev.setUserEmail(principal.getName());

        EvModel savedEv = evService.addEv(ev); // service saves
        return ResponseEntity.ok(savedEv);
    }

    // ✅ FETCH ALL EVs (ADMIN / DEBUG)
}
