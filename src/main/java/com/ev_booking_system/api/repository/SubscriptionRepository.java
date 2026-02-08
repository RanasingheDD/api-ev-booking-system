package com.ev_booking_system.api.repository;

import com.ev_booking_system.api.model.SubscriptionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<SubscriptionModel, String> {

    Optional<SubscriptionModel> findByUserIdAndActiveTrue(String userId);
}
