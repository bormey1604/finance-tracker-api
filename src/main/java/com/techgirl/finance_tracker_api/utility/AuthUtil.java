package com.techgirl.finance_tracker_api.utility;

import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.service.JwtService;
import com.techgirl.finance_tracker_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final JwtService jwtService;
    private final UserService userService;

    public AuthUtil(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public MyUser getAuthenticatedUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            MyUser user = userService.findByUsername(username);

            if (user != null) {
                return user;
            } else {
                return null;
            }
        }

        return null;
    }

}
