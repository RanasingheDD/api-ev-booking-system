package com.ev_booking_system.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ev_booking_system.api.model.SessionModel;
import com.ev_booking_system.api.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    @GetMapping("/sessions/{username}")
    public ResponseEntity<List<SessionModel>> getActiveSessions(@PathVariable String username) {
        return ResponseEntity.ok(service.getActiveSessions(username));
    }

    // Logout specific device
    @DeleteMapping("/{id}")
    public void logoutDevice(@PathVariable("id") String id) {
        service.logoutSession(id);
    }

    // Logout all devices
    @DeleteMapping("/all")
    public void logoutAll(Authentication auth) {
        service.logoutAll(auth.getName());
    }
}
