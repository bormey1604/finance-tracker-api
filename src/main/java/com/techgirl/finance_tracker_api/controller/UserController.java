package com.techgirl.finance_tracker_api.controller;

import com.techgirl.finance_tracker_api.model.response.ApiResponse;
import com.techgirl.finance_tracker_api.model.request.LoginRequest;
import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.service.JwtService;
import com.techgirl.finance_tracker_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody MyUser user) {
        userService.register(user);
        return ResponseEntity.ok(new ApiResponse("200","Successful register!"));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest user) {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) {

            String token = jwtService.generateToken(user.getUsername());
            String expiry = String.valueOf(jwtService.extractExpiration(token));

            Map<String,Object> map = Map.of("token",token,"expiredAt",expiry);

            return ResponseEntity.ok(new ApiResponse("200","Successful login!",map));
        }

        throw new UsernameNotFoundException("Invalid username or password");

    }
}
