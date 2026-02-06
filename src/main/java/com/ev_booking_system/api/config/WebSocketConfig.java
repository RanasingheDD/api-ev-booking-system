package com.ev_booking_system.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // prefix for subscriptions (frontend subscribes here)
        config.setApplicationDestinationPrefixes("/app"); // prefix for sending messages from client to server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // endpoint for WebSocket handshake
                .setAllowedOriginPatterns(
                        "http://localhost:5173",
                        "https://ev-station-booking.vercel.app"
                ) // allow your frontend origins
                .withSockJS(); // fallback for browsers without WebSocket
    }
}
