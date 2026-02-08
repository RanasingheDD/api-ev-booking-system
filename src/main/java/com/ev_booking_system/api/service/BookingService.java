package com.ev_booking_system.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.ev_booking_system.api.Util.JwtUtil;
import com.ev_booking_system.api.dto.BookingQuoteDto;
import com.ev_booking_system.api.model.BookingModel;
import com.ev_booking_system.api.model.BookingModel.BookingStatus;
import com.ev_booking_system.api.model.ChargerModel;
import com.ev_booking_system.api.model.StationModel;
import com.ev_booking_system.api.repository.BookingRepository;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private StationService stationService;

    @Autowired
    private JwtUtil jwtUtil;

    // Generate booking quote
    public BookingQuoteDto getQuote(String chargerId, String stationId, Instant startAt, Instant endAt) {

        double hours = (endAt.getEpochSecond() - startAt.getEpochSecond()) / 3600.0;
        double rate = 1200;
        double estimatedEnergy = hours * rate * 0.1;

        // Check if station is open
        StationModel station = stationService.getStationById(stationId);
        boolean availability = station.isOpen();
        String unavailableReason = null;

        // If station is closed
        if (!availability) {
            unavailableReason = "Station is closed";
        }

        // Check if slot already booked
        boolean isBooked = bookingRepo.existsBookingInTimeRange(
                stationId, chargerId, startAt, endAt);

        if (isBooked) {
            availability = false;
            unavailableReason = "Slot is not available";
        }

        return BookingQuoteDto.builder()
                .chargerId(chargerId)
                .stationId(stationId)
                .startAt(startAt)
                .endAt(endAt)
                .estimatedEnergy(estimatedEnergy)
                .estimatedCost(hours * rate)
                .currency("LKR")
                .available(availability)
                .unavailableReason(unavailableReason)
                .build();
    }

    // Create booking
    public BookingModel createBooking(BookingModel booking, String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String id = jwtUtil.extractUserId(token);
        getQuote(booking.getChargerId(), booking.getStationId(), booking.getStartAt(), booking.getEndAt());
        double hours = (booking.getEndAt().getEpochSecond() - booking.getStartAt().getEpochSecond()) / 3600.0;

        double rate = 1200;

        double estimatedEnergy = hours * rate * 0.1;
        booking.setUserId(id);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(Instant.now());
        booking.setEstimatedCost(hours * rate);
        booking.setFinalCost(hours * rate);

        StationModel station = stationService.getStationById(booking.getStationId());
        ChargerModel charger = stationService.getChargerById(booking.getStationId(), booking.getChargerId());

        booking.setStation(station);
        booking.setCharger(charger);

        /*
         * return BookingMapper.toModel(
         * booking,
         * stationDto,
         * )
         */
        return bookingRepo.save(booking);
    }

    // Get booking by ID
    public BookingModel getById(String bookingId) {
        return bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    // Get user bookings
    @Cacheable(value = "userBookings", key = "#p0")
    public List<BookingModel> getUserBookings(String token, String status) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtUtil.extractUserId(token);
        System.out.println(userId);
        if (status != null && !status.isEmpty()) {

            try {
                BookingStatus bookingStatus = BookingModel.BookingStatus.valueOf(status.toUpperCase());

                return bookingRepo.findByUserIdAndStatus(userId, bookingStatus);

            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid booking status: " + status);
            }
        }

        return bookingRepo.findByUserId(userId);
    }

    // Cancel booking
    public BookingModel cancelBooking(String bookingId, String reason) {
        BookingModel booking = getById(bookingId);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setFinalCost(booking.getFinalCost());

        return bookingRepo.save(booking);
    }

    // Basic availability check
    public boolean checkAvailability(String chargerId, Instant startAt, Instant endAt) {
        List<BookingModel> all = bookingRepo.findAll();

        return all.stream()
                .noneMatch(b -> b.getChargerId().equals(chargerId) && b.getStatus().equals(BookingStatus.CONFIRMED) &&
                        !(b.getEndAt().isBefore(startAt) || b.getStartAt().isAfter(endAt)));
    }
}
