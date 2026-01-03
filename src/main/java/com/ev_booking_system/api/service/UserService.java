package com.ev_booking_system.api.service;

import com.ev_booking_system.api.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.dto.EvDto;
import com.ev_booking_system.api.dto.UserDto;
import com.ev_booking_system.api.mapper.EvMapper;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvService evService;
    @Autowired
    private EvRepository evRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SessionService sessionService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserModel registerUser(UserModel user) {
        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered!");
        }
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPoints(0);
        return userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole());
                    dto.setEvIds(user.getEvIds());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto loginUser(String email, String password) {
        UserModel user = userRepository.findByEmail(email);
        System.out.println("LOGIN EMAIL = " + email);

        if (user == null) {
            System.out.println("USER = " + user);
            throw new RuntimeException("User not found");

        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getId());

        // Return UserDto (safe data)
        return new UserDto(token,user.getId(), user.getName(), user.getEmail(), user.getMobile(),user.getRole(),user.getPoints(),user.getEvIds());
    }

    public EvModel addEV(EvModel evModel,String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        evModel.setUserId(jwtUtil.extractUserId(token));
        return evService.addEV(evModel,token);

    }

    public UserModel getCurrentUser(String token) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String username = auth.getName();
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
    
            String userId = jwtUtil.extractUserId(token);
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();   // <--- print exception!
            return null;
        }
    }
    public List<EvDto> getUserEv(String token){
        try {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtUtil.extractUserId(token);
        return evRepository.findByUserId(userId).stream()
                .map(EvMapper::toDto).collect(Collectors.toList());

    } catch (Exception e) {
        e.printStackTrace();   // <--- print exception!
        return Collections.emptyList();
        }
    }

    public UserDto updateUser(String email, UserDto updatedUser) {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        UserModel savedUser = userRepository.save(user);

        UserDto dto = new UserDto();
        dto.setName(savedUser.getName());


        return dto;
    }

    public void deleteUser(String email) {
        sessionService.invalidateAll(email);
        UserModel user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        }
    }

}
