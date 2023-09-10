package com.project.passwordmanager.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;

    public LoginResponse(String jwtToken) {
        this.jwtToken=jwtToken;
    }
}
