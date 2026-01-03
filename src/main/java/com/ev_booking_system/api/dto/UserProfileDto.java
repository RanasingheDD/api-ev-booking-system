package com.ev_booking_system.api.dto;

import com.ev_booking_system.api.model.Role;

import lombok.Data;

@Data
public class UserProfileDto {

    private String username;
    private String email;
    private long mobile;
    private Role role;
}
