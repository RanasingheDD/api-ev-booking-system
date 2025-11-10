package com.ev_booking_system.api.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserModel {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private Role role; // USER, OWNER, ADMIN
    private List<String> evIds; // references to EV documents

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<String> getEvIds() { return evIds; }
    public void setEvIds(List<String> evIds) { this.evIds = evIds; }
}
