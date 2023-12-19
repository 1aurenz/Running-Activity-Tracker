package com.codecool.runningactivitytracker.repository;

import com.codecool.runningactivitytracker.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.String.format;

@Repository
public class UserRepository {

    private final ConcurrentMap<String, UserEntity> users = new ConcurrentHashMap<>();

    public synchronized Optional<UserEntity> findUserByName(String userName) {
        return Optional.ofNullable(users.get(userName));
    }

    public synchronized void createUser(UserEntity user) {
        String userName = user.getUsername();
        if (users.containsKey(userName)) {
            throw new IllegalArgumentException(format("user %s already exists", userName));
        }
        users.put(userName, user);
    }
}
