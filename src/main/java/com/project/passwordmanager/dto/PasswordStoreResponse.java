package com.project.passwordmanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PasswordStoreResponse {
    private String website;
    private String userName;
    private String password;

    public PasswordStoreResponse(String website, String userName, String password) {
        this.setUserName(userName);
        this.setPassword(password);
        this.setWebsite(website);
    }
}
