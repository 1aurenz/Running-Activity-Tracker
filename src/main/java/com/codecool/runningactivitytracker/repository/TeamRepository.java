package com.codecool.runningactivitytracker.repository;

import com.codecool.runningactivitytracker.entity.TeamEntity;
import com.codecool.runningactivitytracker.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class TeamRepository {

    private final ConcurrentMap<String, TeamEntity> teams = new ConcurrentHashMap<>();

    public Optional<TeamEntity> findTeamByAdmin(String userName) {
        return teams.values().stream()
                .filter(team -> team.getAdmin().getUsername().equals(userName))
                .findFirst();
    }

    public Optional<TeamEntity> findTeamByMember(String memberName) {
        return teams.values().stream()
                .filter(team -> hasMemberWithName(team, memberName))
                .findFirst();
    }

    private boolean hasMemberWithName(TeamEntity team, String memberName) {
        return team.getMembers().stream().anyMatch(member -> member.getUsername().equals(memberName));
    }

    public void createTeam(TeamEntity team) {
        if (teams.containsKey(team.getName())) {
            throw new IllegalArgumentException("team %s already exists");
        }
        teams.put(team.getName(), team);
    }

    public void clear() {
        teams.clear();
    }

    public void addTeamMember(TeamEntity team, UserEntity member) {
        TeamEntity oldTeam = teams.get(team.getName());
        List<UserEntity> memberListCopy = new ArrayList<>(oldTeam.getMembers());
        memberListCopy.add(member);
        TeamEntity updatedTeam = new TeamEntity(team.getName(), memberListCopy, team.getAdmin());
        teams.put(team.getName(), updatedTeam);
    }
}
