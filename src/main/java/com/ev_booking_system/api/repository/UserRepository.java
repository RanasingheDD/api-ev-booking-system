package com.ev_booking_system.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ev_booking_system.api.model.UserModel;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {

    UserModel findByEmail(String email);

    boolean existsByEmail(String email);

    // Optional<UserModel> findById(String id);

}
