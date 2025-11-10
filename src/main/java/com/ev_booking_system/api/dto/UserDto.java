package com.ev_booking_system.api.dto;

import java.util.List;
import com.ev_booking_system.api.model.Role;

public class UserDto {
    private String id;
    private String username;
    private String email;
    private Role role; // USER, OWNER, ADMIN
    private List<String> evIds; // references to EV documents

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<String> getEvIds() { return evIds; }
    public void setEvIds(List<String> evIds) { this.evIds = evIds; }
}
