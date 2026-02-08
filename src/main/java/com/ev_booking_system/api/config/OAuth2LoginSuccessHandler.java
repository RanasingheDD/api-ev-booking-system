package com.ev_booking_system.api.config;

import com.ev_booking_system.api.Util.JwtUtil;
import com.ev_booking_system.api.model.UserModel;
import com.ev_booking_system.api.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        System.out.println("OAuth2 Login Success for email: " + email);

        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            System.err.println("User not found in DB after OAuth success! Email: " + email);
            // handle error, maybe redirect to error page
            response.sendRedirect("https://ev-station-booking.vercel.app/login?error=user_not_found");
            return;
        }

        String token = jwtUtil.generateToken(user);

        // Change this URL to your frontend's redirect URL
        String targetUrl = UriComponentsBuilder.fromUriString("https://ev-station-booking.vercel.app/oauth2/redirect")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
