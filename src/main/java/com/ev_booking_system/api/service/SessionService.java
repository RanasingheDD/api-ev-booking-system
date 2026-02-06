package com.ev_booking_system.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.model.SessionModel;
import com.ev_booking_system.api.repository.SessionRepository;

@Service
public class SessionService {

    private final SessionRepository repo;
    private final SimpMessagingTemplate messagingTemplate;

    public SessionService(SessionRepository repo, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    // Save new login session
    public void createSession(String username, String device, String os, String ip, String token) {
        System.out.println("Creating session for " + username + " on device " + device + " with IP " + ip);
        if (ip == null || ip.isEmpty()) {
            ip = "Unknown IP";
        }

        SessionModel session = new SessionModel();
        session.setUsername(username);
        session.setDevice(device);
        session.setOs(os);
        session.setIp(ip);
        session.setToken(token);
        session.setLastActive(LocalDateTime.now());
        session.setActive(true);

        repo.save(session);
        System.out.println("Session saved!");
        // Broadcast updated sessions
        messagingTemplate.convertAndSend("/topic/sessions/" + username, repo.findByUsernameAndActiveTrue(username));
    }

    // Get active sessions
    public List<SessionModel> getActiveSessions(String username) {
        return repo.findByUsernameAndActiveTrue(username);
    }

    // Logout one device
    public void logoutSession(String sessionId) {
        SessionModel session = repo.findById(sessionId).orElse(null);
        if (session != null) {
            repo.deleteById(sessionId);
            messagingTemplate.convertAndSend("/topic/sessions/" + session.getUsername(), repo.findByUsernameAndActiveTrue(session.getUsername()));
        }
    }

    // Logout all devices
    public void logoutAll(String username) {
        repo.deleteByUsername(username);
        messagingTemplate.convertAndSend("/topic/sessions/" + username, repo.findByUsernameAndActiveTrue(username));
    }

    // Invalidate all sessions (e.g., on account deletion)
    public void invalidateAll(String username) {
        repo.deleteByUsername(username);
        messagingTemplate.convertAndSend("/topic/sessions/" + username, repo.findByUsernameAndActiveTrue(username));
    }

}
