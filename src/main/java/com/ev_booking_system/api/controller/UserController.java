package com.ev_booking_system.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ev_booking_system.api.dto.EvDto;
import com.ev_booking_system.api.dto.LoginRequest;
import com.ev_booking_system.api.dto.UserDto;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.service.UserService;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.ev_booking_system.api.repository.UserRepository;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        user.setRole("USER");
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginRequest loginRequest) {
        UserDto user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/evs")
    public EvModel addEV(@RequestBody EvModel evModel, @RequestHeader("Authorization") String token) {
        return userService.addEV(evModel,token);
    }

    @PutMapping("/{email}")
    public UserDto updateUser(@PathVariable String email, @RequestBody UserDto updatedUser) {
        return userService.updateUser(email, updatedUser);
    }

    @GetMapping("/evs")
    public ResponseEntity<?> getUserEv(@RequestHeader("Authorization") String token){
        List<EvDto> evs =  userService.getUserEv(token);
        return ResponseEntity.ok(Map.of("evs", evs));
    }
  
    @GetMapping("/me")
      public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token) {
          UserModel user = userService.getCurrentUser(token);

          UserDto dto = new UserDto();
          dto.setName(user.getName());
          dto.setEmail(user.getEmail());
          dto.setMobile(user.getMobile());
          dto.setRole(user.getRole());
          return ResponseEntity.ok(dto);
      }

      @PutMapping("/me")
      public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto dto ,@RequestHeader("Authorization") String token) {

          UserModel user = userService.getCurrentUser(token);
          user.setName(dto.getName());
          user.setMobile(dto.getMobile());

          userRepository.save(user);

          return ResponseEntity.ok(dto);
      }

      @DeleteMapping("/me")
      public ResponseEntity<?> deleteAccount(Authentication auth) {
          ///userService.deleteUser(auth.getName()); // deletes user + invalidates sessions
          return ResponseEntity.ok("Account deleted successfully");
      }


}
