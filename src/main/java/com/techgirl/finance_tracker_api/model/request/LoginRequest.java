package com.techgirl.finance_tracker_api.model.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
