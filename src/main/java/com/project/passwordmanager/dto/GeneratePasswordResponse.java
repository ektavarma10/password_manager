package com.project.passwordmanager.dto;

public class GeneratePasswordResponse {
    private String password;

    public GeneratePasswordResponse(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
