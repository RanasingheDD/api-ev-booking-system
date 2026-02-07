package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.dto.PaymentDto;
import com.ev_booking_system.api.service.PaymentService;
import com.stripe.model.EventDataObjectDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;


import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173") // Your Vite Port
@RequiredArgsConstructor
public class PaymentController {

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;


    private final PaymentService paymentService;

    @PostMapping("/checkouts")
    public ResponseEntity<?> checkout(
            @RequestBody PaymentDto dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String paymentUrl = paymentService.createCheckoutSession(dto, token);

            return ResponseEntity.ok(
                    Map.of(
                            "paymentUrl", paymentUrl
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Payment initiation failed")
            );
        }
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            // 1. Verify the webhook signature to ensure it's actually from Stripe
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

            // 2. Filter for the specific event we care about
            if ("checkout.session.completed".equals(event.getType())) {
                System.out.println("🔔 Received Checkout Success Event: " + event.getId());

                // 3. Extract the Session object using the GSON fallback
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                Session session = null;

                if (dataObjectDeserializer.getObject().isPresent()) {
                    // This is the standard way (works if versions match)
                    session = (Session) dataObjectDeserializer.getObject().get();
                } else {
                    // This is the Event.GSON way (works even if versions mismatch)
                    System.out.println("⚠️ Deserializer empty. Manually parsing via Event.GSON...");
                    String rawJson = event.getData().getObject().toJson();
                    session = Event.GSON.fromJson(rawJson, Session.class);
                }

                // 4. Execute your business logic
                if (session != null && session.getId() != null) {
                    System.out.println("✅ Successfully parsed Session: " + session.getId());
                    System.out.println("💰 Amount: " + session.getAmountTotal() + " " + session.getCurrency());

                    // Trigger your database update/fulfillment logic
                    paymentService.completePayment(session.getId());
                } else {
                    System.err.println("❌ Failed to parse Session object from Event data.");
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Parsing error");
                }
            }

            // Always return a 200 OK to Stripe so it doesn't keep retrying the webhook
            return ResponseEntity.ok("Success");

        } catch (SignatureVerificationException e) {
            System.err.println("⚠️ Invalid Webhook Signature!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("🚨 Error processing webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook handling failed");
        }
    }

}
