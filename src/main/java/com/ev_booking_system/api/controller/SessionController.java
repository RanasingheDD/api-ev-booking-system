package com.ev_booking_system.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.ev_booking_system.api.model.SessionModel;
import com.ev_booking_system.api.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    // GET active devices
    @GetMapping
    public List<SessionModel> getSessions(Authentication auth) {
        return service.getUserSessions(auth.getName());
    }

    // Logout specific device
    @DeleteMapping("/{id}")
    public void logoutDevice(@PathVariable String id) {
        service.logoutSession(id);
    }

    // Logout all devices
    @DeleteMapping("/all")
    public void logoutAll(Authentication auth) {
        service.logoutAll(auth.getName());
    }
}
