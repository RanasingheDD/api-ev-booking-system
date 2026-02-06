package com.ev_booking_system.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean active;

}
