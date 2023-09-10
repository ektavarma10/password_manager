package com.project.passwordmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PreviewVaultResponse {
    private List<Entry> entries;

    @Data
    @AllArgsConstructor
    public static class Entry {
        private String username;
        private String website;
    }
}
