package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.model.MessageModel;
import com.ev_booking_system.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageRepository messageRepository;

    // 1. Public Endpoint: Send a Message (Used by ContactUs.tsx)
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageModel message) {
        messageRepository.save(message);
        return ResponseEntity.ok(Map.of("message", "Message sent successfully!"));
    }

    // 2. Admin Endpoint: Get All Messages
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MessageModel>> getAllMessages() {
        // Returns newest messages first
        List<MessageModel> messages = messageRepository.findAll();
        // You can sort by date here if needed
        return ResponseEntity.ok(messages);
    }

    // 3. Admin Endpoint: Delete a Message (Cleanup)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteMessage(@PathVariable String id) {
        messageRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Message deleted"));
    }
}