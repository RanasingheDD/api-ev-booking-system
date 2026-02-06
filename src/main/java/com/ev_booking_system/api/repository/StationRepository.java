package com.ev_booking_system.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ev_booking_system.api.model.StationModel;

@Repository
public interface StationRepository extends MongoRepository<StationModel, String> {

    @Query("{ '$or': [ {'name': { $regex: ?0, $options: 'i' }}, {'address': { $regex: ?0, $options: 'i' }} ] }")
    List<StationModel> searchByKeyword(String keyword);

    /**
     * Find all stations owned by a specific operator
     * @param operatorId - The operator/owner ID
     * @return List of stations
     */
    List<StationModel> findByOperatorId(String operatorId);

    /**
     * Find stations by name (case-insensitive)
     * @param name - Station name
     * @return List of stations
     */
    List<StationModel> findByNameContainingIgnoreCase(String name);

    /**
     * Find stations by address (case-insensitive)
     * @param address - Station address
     * @return List of stations
     */
    List<StationModel> findByAddressContainingIgnoreCase(String address);

}
