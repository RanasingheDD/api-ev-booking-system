package com.ev_booking_system.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.Util.JwtUtil;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;

@Service
public class EvService {

    @Autowired
    private EvRepository evRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public EvModel addEV(EvModel evModel, String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtUtil.extractUserId(token);
        evModel.setUserId(userId);

        return evRepository.save(evModel);
    }

    public EvModel getEvById(String id) {
        return evRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EV not found"));
    }

    public boolean deleteUserEv(String token, String evId) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtUtil.extractUserId(token);
        Optional<EvModel> ev = evRepository.findById(evId);

        if (ev.isPresent() && ev.get().getUserId().equals(userId)) {
            evRepository.deleteById(evId);
            return true;
        }
        return false;
    }
}