package com.codecool.runningactivitytracker.controller;

import com.codecool.runningactivitytracker.entity.UserEntity;
import com.codecool.runningactivitytracker.repository.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Data
    private static class CreateUserRequest {
        private String username;
        private String password;
    }

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/internal/user")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        UserEntity user = new UserEntity(request.getUsername(), request.getPassword());
        userRepository.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    public String me() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return "Hello " + user.getUsername();
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public\n";
    }
}
