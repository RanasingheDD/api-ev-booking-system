package com.ev_booking_system.api.service;

import com.ev_booking_system.api.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ev_booking_system.api.dto.UserDto;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvRepository evRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserModel registerUser(UserModel user) {
        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered!");
        }
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        return userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
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

        String token = jwtUtil.generateToken(user.getEmail());

        // Return UserDto (safe data)
        return new UserDto(token,user.getId(), user.getUsername(), user.getEmail(), user.getRole(),user.getEvIds());
    }

    public EvModel addEV(EvModel evModel) {
        // Optional: check if registrationNo already exists
        /*if(evRepository.findByRegistrationNo(evModel.getRegistrationNo()) != null){
            throw new RuntimeException("EV with this registration number already exists");
        }*/
        return evRepository.save(evModel);

        //return "addeds";
    }

    public UserModel getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByEmail(username); // or findByUsername()
    }

    public EvModel findEvForCurrentUser() {
        UserModel user = getCurrentUser();
        String id = user.getId();
        return evRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EV not found for user: " + id));
    }

    public UserDto updateUser(String email, UserDto updatedUser) {
        // Find existing user
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Update fields
        if (updatedUser.getUsername() != null) {
            user.setUsername(updatedUser.getUsername());
        }
        // Optionally allow updating email:
        // if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());

        // Save updated user
        UserModel savedUser = userRepository.save(user);

        // Convert to UserDto
        UserDto dto = new UserDto();
        dto.setUsername(savedUser.getUsername());
        // add other fields if needed

        return dto;
    }

}
