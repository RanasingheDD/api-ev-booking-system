package com.ev_booking_system.api.repository;

import com.ev_booking_system.api.model.PaymentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentModel, String> {

    Optional<PaymentModel> findByStripeSessionId(String stripeSessionId);
}
