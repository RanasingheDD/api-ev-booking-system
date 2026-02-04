package com.ev_booking_system.api.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sessions")
public class SessionModel {

    @Id
    private String id;

    private String username;
    private String device;
    private String os;
    private String ip;
    private String token;
    private LocalDateTime lastActive;

    // getters & setters
}
