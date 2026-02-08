package com.ev_booking_system.api.service;

import com.ev_booking_system.api.dto.PaymentDto;
import com.ev_booking_system.api.model.PaymentModel;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.PaymentRepository;
import com.ev_booking_system.api.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ev_booking_system.api.Util.JwtUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

        private final PaymentRepository paymentRepository;

        @Autowired
        private final UserRepository userRepository;

        @Value("${stripe.secret.key}")
        private String stripeSecretKey;

        @Autowired
        private JwtUtil jwtUtil;

        public String createCheckoutSession(PaymentDto dto, String token) throws Exception {

                // Extract userId from token
                if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                }
                String userId = jwtUtil.extractUserId(token);

                PaymentModel payment = PaymentModel.builder()
                                .userId(userId)
                                .points(dto.getPoints())
                                .price(dto.getPrice())
                                .status("PENDING")
                                .build();

                // Save it to get the generated ID
                PaymentModel savedPayment = paymentRepository.save(payment);

                // Stripe Checkout Session
                Stripe.apiKey = stripeSecretKey;

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl("https://ev-station-booking.vercel.app/payment-success?id="
                                                + savedPayment.getId())
                                .setCancelUrl("https://ev-station-booking.vercel.app/cancel")
                                .addLineItem(
                                                SessionCreateParams.LineItem.builder()
                                                                .setQuantity(1L)
                                                                .setPriceData(
                                                                                SessionCreateParams.LineItem.PriceData
                                                                                                .builder()
                                                                                                .setCurrency("lkr")
                                                                                                .setUnitAmount((long) (dto
                                                                                                                .getPrice()
                                                                                                                * 100))
                                                                                                .setProductData(
                                                                                                                SessionCreateParams.LineItem.PriceData.ProductData
                                                                                                                                .builder()
                                                                                                                                .setName(dto.getPoints()
                                                                                                                                                + " Points")
                                                                                                                                .build())
                                                                                                .build())
                                                                .build())
                                .build();

                Session session = Session.create(params);

                savedPayment.setStripeSessionId(session.getId());
                paymentRepository.save(savedPayment);

                return session.getUrl();
        }

        @Transactional
        public void completePayment(String stripeSessionId) {

                System.out.println("4:Complete payment for session: " + stripeSessionId);
                PaymentModel payment = paymentRepository
                                .findByStripeSessionId(stripeSessionId)
                                .orElseThrow(() -> new RuntimeException("Payment not found"));

                if (!"SUCCESS".equals(payment.getStatus())) {
                        System.out.println("5:Payment status is: " + payment.getStatus());
                        payment.setStatus("SUCCESS"); // mark as paid
                        paymentRepository.save(payment);

                        UserModel user = userRepository.findById(payment.getUserId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "User not found with ID: " + payment.getUserId()));

                        // 4. Update and Save
                        user.setPoints(user.getPoints() + payment.getPoints());
                        userRepository.save(user);
                }
        }

}
