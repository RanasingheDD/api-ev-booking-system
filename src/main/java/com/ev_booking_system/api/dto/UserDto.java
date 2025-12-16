package com.ev_booking_system.api.dto;

import java.util.List;

import com.ev_booking_system.api.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String token;
    private String id;
    private String name;
    private String email;
    private Role role; // USER, OWNER, ADMIN
    private List<String> evIds; // references to EV documents
}
