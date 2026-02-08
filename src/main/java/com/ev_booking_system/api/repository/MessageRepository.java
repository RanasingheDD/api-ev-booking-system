package com.ev_booking_system.api.repository;

import com.ev_booking_system.api.model.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<MessageModel, String> {
    List<MessageModel> findByIsReadFalse(); // Find unread messages
}