package com.ev_booking_system.api.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;
<<<<<<< HEAD
import com.ev_booking_system.api.service.EvService;
=======

>>>>>>> 814ccf865012c161873a8f82f5ceb6d52b7fc4fb


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
<<<<<<< HEAD
  
=======
>>>>>>> 814ccf865012c161873a8f82f5ceb6d52b7fc4fb
}

