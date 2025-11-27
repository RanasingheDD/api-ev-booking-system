package com.ev_booking_system.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ev_booking_system.api.model.StationModel;

@Repository
public interface StationRepository extends MongoRepository<StationModel, String> {

    StationModel findByUsername(String username);
}
