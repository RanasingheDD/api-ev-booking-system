package com.ev_booking_system.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentModel {

    @Id
    private String id;

    private String userId;
    private int points;
    private double price;

    private String stripeSessionId;
    private String status; // PENDING, SUCCESS, FAILED
}
