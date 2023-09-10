package com.project.passwordmanager.controller;


import com.project.passwordmanager.dto.LoginRequest;
import com.project.passwordmanager.dto.LoginResponse;
import com.project.passwordmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public void register(@RequestBody LoginRequest request) {
        userService.register(request.getUsername(), request.getPassword());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
         return ResponseEntity.ok(new LoginResponse(userService.authenticate(request.getUsername(), request.getPassword())));
    }
}
