package com.ev_booking_system.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
