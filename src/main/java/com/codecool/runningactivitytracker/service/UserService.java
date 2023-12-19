package com.codecool.runningactivitytracker.service;

import com.codecool.runningactivitytracker.entity.UserEntity;
import com.codecool.runningactivitytracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findCurrentUser() {
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String username = contextUser.getUsername();
        return userRepository.findUserByName(username)
                .orElseThrow(() -> new IllegalArgumentException(format("could not find user %s in the repository", username)));

    }
}
