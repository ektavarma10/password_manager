package com.project.passwordmanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetPasswordRequest {
    private String website;
    private String userId;
    private String loginPassword;
}
