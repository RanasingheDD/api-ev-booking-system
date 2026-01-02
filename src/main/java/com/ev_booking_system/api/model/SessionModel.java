package com.ev_booking_system.api.model;

//import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
//@Entity
@Document(collection = "sessions")
@NoArgsConstructor
@AllArgsConstructor
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
