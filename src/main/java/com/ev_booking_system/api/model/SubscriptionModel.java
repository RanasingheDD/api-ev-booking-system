package com.ev_booking_system.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionModel {

    @Id
    private String id;

    private String userId;

    private String planId;
    private String planName;

    private Instant startDate;
    private Instant expiresAt;

    private boolean active;
}
