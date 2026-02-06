package com.ev_booking_system.api.config;

import com.ev_booking_system.api.model.AuthProvider;
import com.ev_booking_system.api.model.Role;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        UserModel user = userRepository.findByEmail(email);
        if (user != null) {
            if (!AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
               // You might want to update the provider or handle account linking here.
               // For simplicity, we'll just update the provider key.
                 user.setAuthProvider(AuthProvider.GOOGLE);
                 user.setProviderId(oAuth2User.getAttribute("sub"));
                 userRepository.save(user);
            }
             user = updateExistingUser(user, oAuth2User);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2User);
        }

        return oAuth2User;
    }

    private UserModel registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        UserModel user = new UserModel();
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId(oAuth2User.getAttribute("sub"));
        user.setName(oAuth2User.getAttribute("name"));
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    private UserModel updateExistingUser(UserModel existingUser, OAuth2User oAuth2User) {
        existingUser.setName(oAuth2User.getAttribute("name"));
        return userRepository.save(existingUser);
    }
}
