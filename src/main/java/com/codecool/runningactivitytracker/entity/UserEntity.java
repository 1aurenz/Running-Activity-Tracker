package com.codecool.runningactivitytracker.entity;

import lombok.Value;

@Value
public class UserEntity {

    String username;
    String password;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
