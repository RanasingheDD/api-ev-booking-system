package com.ev_booking_system.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ev_booking_system.api.model.BookingModel;
import com.ev_booking_system.api.model.BookingModel.BookingStatus;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends MongoRepository<BookingModel, String> {
    List<BookingModel> findByUserId(String userId);
    List<BookingModel> findByUserIdAndStatus(String userId, BookingStatus status);
    //BookingModel findById(String bookingId);
    
    @Query(value = "{" +
            "'stationId': ?0, " +
            "'chargerId': ?1, " +
            "'status': {$in: ['PENDING', 'COMPLETED']}, " +
            "'startAt': {$lt: ?3}, " +
            "'endAt': {$gt: ?2}" +
            "}", 
            exists = true)

    boolean existsBookingInTimeRange(
            String stationId,
            String chargerId,
            Instant startAt,
            Instant endAt
    );
}
