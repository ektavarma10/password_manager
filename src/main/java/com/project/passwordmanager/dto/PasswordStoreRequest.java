package com.project.passwordmanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PasswordStoreRequest {
    private String userId;
    private String loginPassword;

    private String website;
    private String username;
    private String password;
}
