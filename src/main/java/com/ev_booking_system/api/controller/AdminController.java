package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final AdminService adminService;

    // --- 📊 DASHBOARD STATS ---
    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // --- 👥 USER MANAGEMENT ---

    // 1. Get All Users
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // 2. Register Owner
    @PostMapping("/register-owner")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registerOwner(@RequestBody UserModel ownerData) {
        try {
            adminService.registerOwner(ownerData);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Owner account created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Email likely already exists.");
        }
    }

    // --- 🔌 STATION MANAGEMENT ---

    // 1. Get All Stations
    @GetMapping("/stations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<StationModel>> getAllStations() {
        return ResponseEntity.ok(adminService.getAllStations());
    }

    // 2. Get Pending Delete Requests
    @GetMapping("/stations/delete-requests")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<StationModel>> getDeleteRequests() {
        return ResponseEntity.ok(adminService.getStationsPendingDelete());
    }

    // 3. Confirm Delete
    @DeleteMapping("/stations/{id}/confirm")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> confirmDelete(@PathVariable String id) {
        adminService.confirmStationDelete(id);
        return ResponseEntity.ok(Map.of("message", "Station deleted permanently."));
    }

    // 4. Reject Delete
    @PutMapping("/stations/{id}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> rejectDelete(@PathVariable String id) {
        adminService.rejectStationDelete(id);
        return ResponseEntity.ok(Map.of("message", "Delete request rejected. Station remains active."));
    }
}