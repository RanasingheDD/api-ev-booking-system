package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.service.BookingService;
import com.ev_booking_system.api.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ev_booking_system.api.dto.BookingQuoteDto;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

        @Autowired
        private BookingService bookingService;

        @PostMapping("/checkout")
        public ResponseEntity<Map<String, String>> createCheckoutSession(
                        @RequestBody BookingModel bookingRequest,
                        @RequestHeader("Authorization") String token) throws StripeException {

                BookingModel savedBooking = bookingService.createBooking(bookingRequest, token);
                System.out.println("booking created");
                return ResponseEntity.ok(Map.of("booking_id", savedBooking.getId(), "status",
                                savedBooking.getStatus().toString()));
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

        @GetMapping("/check")
        public ResponseEntity<BookingQuoteDto> checkBooking(
                        @RequestParam(name = "stationId", required = false) String stationId,
                        @RequestParam("chargerId") String chargerId,
                        @RequestParam("startTime") Instant startTime,
                        @RequestParam("endTime") Instant endTime) {

                return ResponseEntity.ok(bookingService.getQuote(chargerId, stationId, startTime, endTime));
        }

        @PostMapping("{bookingId}/cancel")
        public ResponseEntity<BookingModel> cancelBooking(@PathVariable("bookingId") String bookingId,
                        @RequestBody String reason) {
                return ResponseEntity.ok(bookingService.cancelBooking(bookingId, ""));
        }

}
