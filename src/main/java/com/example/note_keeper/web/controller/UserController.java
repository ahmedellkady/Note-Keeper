package com.example.note_keeper.web.controller;

import com.example.note_keeper.application.dto.*;
import com.example.note_keeper.application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
