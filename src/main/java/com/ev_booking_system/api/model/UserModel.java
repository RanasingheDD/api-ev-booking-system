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

    private String username;
    private String email;
    private long mobile;
    private String password;
    private String address;
    private Role role; // USER, OWNER, ADMIN
    private List<String> evIds; // references to EV documents

    public void setRole(String user) {
        this.role = role;
    }

}
