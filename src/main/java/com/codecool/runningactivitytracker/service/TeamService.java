package com.codecool.runningactivitytracker.service;

import com.codecool.runningactivitytracker.entity.TeamEntity;
import com.codecool.runningactivitytracker.entity.UserEntity;
import com.codecool.runningactivitytracker.repository.TeamRepository;
import com.codecool.runningactivitytracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public TeamService(TeamRepository teamRepository,
                       UserRepository userRepository,
                       UserService userService) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void createTeam(String teamName) {
        UserEntity currentUser = userService.findCurrentUser();
        TeamEntity newTeam = new TeamEntity(teamName, List.of(currentUser), currentUser);
        teamRepository.createTeam(newTeam);
    }

    public void addMember(String memberName) {
        UserEntity member = userRepository.findUserByName(memberName)
                .orElseThrow(() -> new IllegalArgumentException(format("user %s not found", memberName)));
        if (teamRepository.findTeamByMember(memberName).isPresent()) {
            throw new IllegalArgumentException(format("user %s is already a member of a team", memberName));
        }
        String currentUserName = userService.findCurrentUser().getUsername();
        TeamEntity team = teamRepository.findTeamByAdmin(currentUserName)
                .orElseThrow(() -> new IllegalStateException(format("user %s is not the administrator of any teams", currentUserName)));
        teamRepository.addTeamMember(team, member);
    }
}
