package com.ev_booking_system.api.service;

import com.ev_booking_system.api.model.Role;
import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.BookingRepository;
import com.ev_booking_system.api.repository.StationRepository;
import com.ev_booking_system.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService; // To reuse registration logic

    // --- 📊 DASHBOARD STATS ---
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalStations = stationRepository.count();
        long totalBookings = bookingRepository.count();
        long pendingApprovals = stationRepository.findByDeleteRequestedTrue().size(); // Using delete requests as "pending actions"

        // Calculate total revenue (Sum of all booking costs)
        double totalRevenue = bookingRepository.findAll().stream()
                .mapToDouble(b -> b.getFinalCost() != null ? b.getFinalCost() : 0)
                .sum();

        stats.put("totalUsers", totalUsers);
        stats.put("totalStations", totalStations);
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        stats.put("pendingApprovals", pendingApprovals);

        return stats;
    }

    // --- 👥 USER MANAGEMENT ---

    // 1. Get All Users
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Register Owner
    public void registerOwner(UserModel owner) {
        owner.setRole(Role.OWNER);
        userService.registerUser(owner); // Hashing & saving happens here
        userRepository.save(owner);
    }

    // --- 🔌 STATION MANAGEMENT ---

    // 1. Get ALL Stations
    public List<StationModel> getAllStations() {
        return stationRepository.findAll();
    }

    // 2. Get Pending Delete Requests
    public List<StationModel> getStationsPendingDelete() {
        return stationRepository.findByDeleteRequestedTrue();
    }

    // 3. Confirm Delete
    public void confirmStationDelete(String stationId) {
        stationRepository.deleteById(stationId);
    }

    // 4. Reject Delete
    public void rejectStationDelete(String stationId) {
        Optional<StationModel> station = stationRepository.findById(stationId);
        if (station.isPresent()) {
            StationModel s = station.get();
            s.setDeleteRequested(false); // Reset flag
            stationRepository.save(s);
        }
    }
}