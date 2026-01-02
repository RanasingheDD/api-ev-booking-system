package com.ev_booking_system.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.ev_booking_system.api.model.SessionModel;
import com.ev_booking_system.api.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private  SessionRepository repo;

    /*public SessionService(SessionRepository repo) {
        this.repo = repo;
    }*/

    // Save new login session
    public void createSession(
            String username,
            String device,
            String os,
            String ip,
            String token
    ) {
        SessionModel session = new SessionModel();
        session.setUsername(username);
        session.setDevice(device);
        session.setOs(os);
        session.setIp(ip);
        session.setToken(token);
        session.setLastActive(LocalDateTime.now());

        repo.save(session);
    }

    // Get active sessions
    public List<SessionModel> getUserSessions(String username) {
        return repo.findByUsername(username);
    }

    // Logout one device
    public void logoutSession(String sessionId) {
        repo.deleteById(sessionId);
    }

    // Logout all devices
    public void logoutAll(String username) {
        repo.deleteByUsername(username);
    }

    // Called when user deletes account
    public void invalidateAll(String username) {
        repo.deleteByUsername(username);
    }

}
