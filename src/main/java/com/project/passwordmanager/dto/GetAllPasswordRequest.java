package com.project.passwordmanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetAllPasswordRequest {
    private String userId;
    private String loginPassword;
}
