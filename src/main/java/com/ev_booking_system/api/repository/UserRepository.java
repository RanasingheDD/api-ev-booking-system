package com.ev_booking_system.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ev_booking_system.api.model.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    UserModel findByEmail(String email);
    UserModel findByUsername(String username);
}
