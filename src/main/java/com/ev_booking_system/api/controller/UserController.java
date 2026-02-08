package com.ev_booking_system.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev_booking_system.api.dto.EvDto;
import com.ev_booking_system.api.dto.LoginRequest;
import com.ev_booking_system.api.dto.UserDto;
import com.ev_booking_system.api.model.PointsRequest;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.model.Role;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.UserRepository;
import com.ev_booking_system.api.service.EvService;
import com.ev_booking_system.api.service.SessionService;
import com.ev_booking_system.api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private EvService evService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        user.setRole(Role.USER); // Default role
        userService.registerUser(user);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    // @PostMapping("/login")
    // public ResponseEntity<UserDto> loginUser(@RequestBody LoginRequest
    // loginRequest) {
    // UserDto user = userService.loginUser(loginRequest.getEmail(),
    // loginRequest.getPassword());
    // if (user != null) {
    // // Save session
    // sessionService.createSession(
    // user.getEmail(), // username
    // loginRequest.getDevice(), // device name from frontend
    // loginRequest.getOs(), // OS info from frontend
    // loginRequest.getIp(), // IP from request
    // user.getToken()
    // );
    // return ResponseEntity.ok(user);
    // } else {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    // }
    // }
    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {

        UserDto user = userService.loginUser(
                loginRequest.getEmail(),
                loginRequest.getPassword());

        if (user != null) {
            sessionService.createSession(
                    user.getEmail(),
                    loginRequest.getDevice(), // device name from frontend
                    loginRequest.getOs(), // OS info from frontend
                    request.getRemoteAddr(), // IP from request
                    user.getToken());

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/evs")
    public EvModel addEV(@RequestBody EvModel evModel, @RequestHeader("Authorization") String token) {
        return userService.addEV(evModel, token);
    }

    @PostMapping("/evs/delete/{evId}")
    public ResponseEntity<?> removeEV(@PathVariable("evId") String evId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(evService.deleteUserEv(token, evId));
    }

    @PutMapping("/{email}")
    public UserDto updateUser(@PathVariable("email") String email, @RequestBody UserDto updatedUser) {
        return userService.updateUser(email, updatedUser);
    }

    @GetMapping("/evs")
    public ResponseEntity<?> getUserEv(@RequestHeader("Authorization") String token) {
        List<EvDto> evs = userService.getUserEv(token);
        return ResponseEntity.ok(Map.of("evs", evs));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token) {
        UserModel user = userService.getCurrentUser(token);

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setPoints(user.getPoints());
        dto.setRole(user.getRole());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("me/points/deduct")
    public ResponseEntity<String> deductPoints(@RequestBody PointsRequest points,
            @RequestHeader("Authorization") String token) {
        System.out.println("points deducted");
        userService.deductUserPoints(token, points.getPoints());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("me/points/add")
    public ResponseEntity<String> addPoints(@RequestBody PointsRequest points,
            @RequestHeader("Authorization") String token) {
        System.out.println("points added");
        userService.addUserPoints(token, points.getPoints());
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto dto,
            @RequestHeader("Authorization") String token) {

        UserModel user = userService.getCurrentUser(token);
        user.setName(dto.getName());
        user.setMobile(dto.getMobile());

        userRepository.save(user);

        return ResponseEntity.ok(dto);
    }

    // @DeleteMapping("/me")
    // public ResponseEntity<?> deleteAccount(Authentication auth) {
    //     /// userService.deleteUser(auth.getName()); // deletes user + invalidates
    //     /// sessions
    //     return ResponseEntity.ok("Account deleted successfully");
    // }
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token) {
        try {
            UserModel user = userService.getCurrentUser(token); // get user from token
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            userService.deleteUser(user.getEmail());

            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete account");
        }
    }

    // Helper methods to parse device and OS from User-Agent
    private String parseDevice(String userAgent) {
        if (userAgent == null) {
            return "Unknown Device";
        }

        if (userAgent.contains("Mobile")) {
            return "Mobile";
        }
        if (userAgent.contains("Windows")) {
            return "PC";
        }
        if (userAgent.contains("Mac")) {
            return "Mac";
        }
        return "Unknown Device";
    }

    private String parseOS(String userAgent) {
        if (userAgent == null) {
            return "Unknown OS";
        }

        if (userAgent.contains("Windows")) {
            return "Windows";
        }
        if (userAgent.contains("Linux")) {
            return "Linux";
        }
        if (userAgent.contains("Android")) {
            return "Android";
        }
        if (userAgent.contains("Mac OS")) {
            return "MacOS";
        }
        return "Unknown OS";
    }

    @GetMapping("/me/points")
    public ResponseEntity<UserDto> getUserPoints(@RequestHeader("Authorization") String token) {
        UserDto user = userService.getUserPoints(token);
        System.out.println(user.getPoints());
        return ResponseEntity.ok(userService.getUserPoints(token));
    }

    // private String parseDevice(String userAgent) {
    // if (userAgent == null) {
    // return "Unknown Device";
    // }
    // if (userAgent.contains("Mobile")) {
    // return "Mobile";
    // }
    // if (userAgent.contains("Windows")) {
    // return "PC";
    // }
    // if (userAgent.contains("Mac")) {
    // return "Mac";
    // }
    // return "Unknown Device";
    // }
    // private String parseOS(String userAgent) {
    // if (userAgent == null) {
    // return "Unknown OS";
    // }
    // if (userAgent.contains("Windows")) {
    // return "Windows";
    // }
    // if (userAgent.contains("Linux")) {
    // return "Linux";
    // }
    // if (userAgent.contains("Android")) {
    // return "Android";
    // }
    // if (userAgent.contains("Mac OS")) {
    // return "MacOS";
    // }
    // return "Unknown OS";
    // }
}
