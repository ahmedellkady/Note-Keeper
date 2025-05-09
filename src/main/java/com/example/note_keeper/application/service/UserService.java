package com.example.note_keeper.application.service;

import com.example.note_keeper.domain.model.User;
import com.example.note_keeper.domain.repository.UserRepository;
import com.example.note_keeper.application.dto.*;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(null, request.getName(), request.getEmail(), request.getPassword());
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
