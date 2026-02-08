package com.ev_booking_system.api.service;

import com.ev_booking_system.api.dto.SubscriptionDto;
import com.ev_booking_system.api.model.SubscriptionModel;
import com.ev_booking_system.api.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    //  Get active subscription
    public SubscriptionDto getUserSubscription(String userId) {
        return subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .map(sub -> SubscriptionDto.builder()
                        .planId(sub.getPlanId())
                        .name(sub.getPlanName())
                        .expiresAt(sub.getExpiresAt().toString())
                        .build())
                .orElse(null);
    }

    //  Purchase subscription
    public SubscriptionDto purchaseSubscription(
            String userId,
            String planId,
            String planName
    ) {
        //  Deactivate old subscription if exists
        subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .ifPresent(old -> {
                    old.setActive(false);
                    subscriptionRepository.save(old);
                });

        Instant now = Instant.now();
        Instant expiry = now.plus(30, ChronoUnit.DAYS);

        SubscriptionModel subscription = SubscriptionModel.builder()
                .userId(userId)
                .planId(planId)
                .planName(planName)
                .startDate(now)
                .expiresAt(expiry)
                .active(true)
                .build();

        subscriptionRepository.save(subscription);

        return SubscriptionDto.builder()
                .planId(planId)
                .name(planName)
                .expiresAt(expiry.toString())
                .build();
    }
}
