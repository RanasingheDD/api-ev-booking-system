package com.ev_booking_system.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserModel {

    @Id
    private String id;

    private String name;
    private String email;
    private long mobile;
    private String password;
    private Role role; // USER, OWNER, ADMIN
    private int points;
    private List<String> evIds; // references to EV documents

    public void setRole(Role role) {
        this.role = role;
    }

}
