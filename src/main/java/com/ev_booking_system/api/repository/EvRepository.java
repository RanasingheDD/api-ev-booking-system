package com.ev_booking_system.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ev_booking_system.api.model.EvModel;

@Repository
public interface EvRepository extends MongoRepository<EvModel, String> {

    EvModel findByRegistrationNo(String registrationNo);

    List<EvModel> findByUserEmail(String userEmail);
}
