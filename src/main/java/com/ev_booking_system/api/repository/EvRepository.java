package com.ev_booking_system.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ev_booking_system.api.model.EvModel;

public interface EvRepository extends MongoRepository<EvModel, String> {
    EvModel findByRegistrationNo(String registrationNo);
}
