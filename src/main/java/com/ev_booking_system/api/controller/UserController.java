package com.ev_booking_system.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ev_booking_system.api.dto.LoginRequest;
import com.ev_booking_system.api.dto.UserDto;
import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.service.UserService;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserModel registerUser(@RequestBody UserModel user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public UserDto loginUser(@RequestBody LoginRequest loginRequest) {
    return userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/addEv")
    public EvModel addEV(@RequestBody EvModel evModel) { 
        return userService.addEv(evModel);
    }
    

    
}
