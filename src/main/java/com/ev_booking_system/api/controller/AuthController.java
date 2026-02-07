package com.ev_booking_system.api.controller;

import com.ev_booking_system.api.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // Remove "Bearer " prefix
            System.out.println("Jiii");
            System.out.println(authHeader);
            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                System.out.println("Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error validating token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
