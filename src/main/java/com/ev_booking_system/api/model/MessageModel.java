package com.ev_booking_system.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class MessageModel {
    @Id
    private String id;
    private String name;
    private String email;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean isRead = false; // To track if Admin saw it
}