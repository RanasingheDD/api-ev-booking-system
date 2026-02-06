package com.ev_booking_system.api.service;

import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Find the user in your MongoDB
        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // 2. 👇 THIS IS THE MAGIC PART
        // We convert your Enum "OWNER" into a Security Authority "OWNER"
        List<GrantedAuthority> authorities;
        if (user.getRole() != null) {
            // We use the exact name (e.g., "OWNER") because your Controller checks hasAuthority('OWNER')
            authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        } else {
            authorities = Collections.emptyList();
        }

        // 3. Return the fully loaded user to Spring Security
        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities // <--- Now this list is NOT empty!
        );
    }
}
