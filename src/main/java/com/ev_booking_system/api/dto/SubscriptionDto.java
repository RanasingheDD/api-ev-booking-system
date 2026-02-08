package com.ev_booking_system.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {

    private String planId;
    private String name;
    private String expiresAt;
}
