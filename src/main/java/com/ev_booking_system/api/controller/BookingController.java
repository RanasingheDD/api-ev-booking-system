package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.stripe.model.checkout.Session;
import com.ev_booking_system.api.model.BookingModel;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Your Vite Port
public class BookingController {

        private final BookingService bookingService;

        @PostMapping("/checkout")
        public ResponseEntity<Map<String, String>> createCheckoutSession(
                        @RequestBody BookingModel bookingRequest,
                        @RequestHeader("Authorization") String token) throws StripeException {

                // 1. Create the booking in DB (Status: PENDING)
                // This calculates the cost based on your Service logic
                BookingModel savedBooking = bookingService.createBooking(bookingRequest, token);

                // 2. Configure Stripe (Use your Secret Key)
                Stripe.apiKey = "";

                // Convert LKR to Cents/Paras (Stripe uses smallest units)
                long amountInCents = (long) (savedBooking.getFinalCost() * 100);
                System.out.println(amountInCents);

    @PostMapping("/points")
    public ResponseEntity<Map<String, String>> createCheckoutSession(
            @RequestBody BookingModel bookingRequest,
            @RequestHeader("Authorization") String token) throws StripeException {

                Session session = Session.create(params);

                Map<String, String> response = new HashMap<>();
                response.put("url", session.getUrl());
                return ResponseEntity.ok(response);
        }

        public ResponseEntity<Map<String, String>> getBookingById(@PathVariable String id) {
                BookingModel booking = bookingService.getById(id);
                Map<String, String> response = new HashMap<>();
                response.put("booking_id", booking.getId());
                response.put("status", booking.getStatus().toString());
                return ResponseEntity.ok(response);
        }

        @GetMapping("/me")
        public ResponseEntity<List<BookingModel>> getUserBookings(
                        @RequestHeader("Authorization") String token) {

                List<BookingModel> bookings = bookingService.getUserBookings(token, null);
                return ResponseEntity.ok(bookings);
        }

        Map<String, String> response = new HashMap<>();
        response.put("url", session.getUrl());
        return ResponseEntity.ok(response);
    }



    @GetMapping("/available-slots")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSlots(
            @RequestParam String chargerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Instant dayStart = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant dayEnd = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return ResponseEntity.ok(
                bookingService.getAvailableSlots(chargerId, dayStart, dayEnd)
        );
    }

}