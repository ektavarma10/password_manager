package com.project.passwordmanager.controller;

import com.project.passwordmanager.dto.*;
import com.project.passwordmanager.service.PasswordStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.List;

@RestController
@RequestMapping("/password")
public class PasswordManagerApi {

    @Autowired
    private PasswordStoreService passwordStoreService;

    @PostMapping("/generate")
    public GeneratePasswordResponse generatePassword() {
        return passwordStoreService.generatePassword();
    }

    @PostMapping ("/")
    public void save(@RequestBody PasswordStoreRequest passwordStoreRequest) {
        passwordStoreService.savePassword(passwordStoreRequest);
    }

    @GetMapping("/preview")
    public PreviewVaultResponse previewVault(ServletRequest request) {
        return passwordStoreService.previewVault((String) request.getAttribute("username"));
    }

    @PostMapping("/show")
    public PasswordStoreResponse getPassword(@RequestBody GetPasswordRequest request ) {
        return passwordStoreService.getPassword(request);
    }

    @PostMapping("/showAll")
    public List<PasswordStoreResponse> getAllPassword(@RequestBody GetAllPasswordRequest request) {
        return passwordStoreService.getAllPassword(request);
    }

}
