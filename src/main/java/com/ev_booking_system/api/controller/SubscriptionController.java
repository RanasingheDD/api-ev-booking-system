package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.dto.SubscriptionDto;
import com.ev_booking_system.api.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    //  Get current user's subscription
    @GetMapping("/me")
    public ResponseEntity<?> getMySubscription(Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(subscriptionService.getUserSubscription(userId));
    }

    // Purchase subscription with points
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSubscription(
            @RequestBody Map<String, Object> body,
            Authentication auth
    ) {
        String userId = auth.getName();

        String planId = body.get("planId").toString();
        String planName = planId.substring(0, 1).toUpperCase() + planId.substring(1);

        SubscriptionDto dto = subscriptionService.purchaseSubscription(
                userId,
                planId,
                planName
        );

        return ResponseEntity.ok(dto);
    }
}
