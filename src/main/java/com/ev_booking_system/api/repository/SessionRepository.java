package com.ev_booking_system.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ev_booking_system.api.model.SessionModel;

public interface SessionRepository extends MongoRepository<SessionModel, String> {

    List<SessionModel> findByUsername(String username);

    void deleteByToken(String token);

    void deleteByUsername(String username);
}
