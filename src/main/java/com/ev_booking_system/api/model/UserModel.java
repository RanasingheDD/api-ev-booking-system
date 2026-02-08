package com.ev_booking_system.api.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String name;
    private String email;
    private long mobile;
    private String password;

    @JsonIgnore
    private Role role; // USER, OWNER, ADMIN

    private int points;
    private List<String> evIds; // references to EV documents

    private AuthProvider authProvider;
    private String providerId;

    public void setRole(Role role) {
        this.role = role;
    }

}
