package com.codecool.runningactivitytracker.entity;

import lombok.Value;

import java.util.List;

@Value
public class TeamEntity {

    String name;
    List<UserEntity> members;
    UserEntity admin;

    public TeamEntity(String name, List<UserEntity> members, UserEntity admin) {
        this.name = name;
        this.members = members;
        this.admin = admin;
    }
}
