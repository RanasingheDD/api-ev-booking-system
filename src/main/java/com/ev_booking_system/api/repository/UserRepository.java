package com.ev_booking_system.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ev_booking_system.api.model.UserModel;

public interface UserRepository extends MongoRepository<UserModel, String> {
    UserModel findByEmail(String email);
}
