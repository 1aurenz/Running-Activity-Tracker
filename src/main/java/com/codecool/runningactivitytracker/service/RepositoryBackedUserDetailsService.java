package com.codecool.runningactivitytracker.service;

import com.codecool.runningactivitytracker.entity.UserEntity;
import com.codecool.runningactivitytracker.repository.TeamRepository;
import com.codecool.runningactivitytracker.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class RepositoryBackedUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    public RepositoryBackedUserDetailsService(
            UserRepository userRepository,
            TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByName(username)
                .orElseThrow(() ->new UsernameNotFoundException(username));
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        if (teamRepository.findTeamByAdmin(username).isPresent()) {
            roles.add(new SimpleGrantedAuthority("ROLE_TEAM_ADMIN"));
        } else if (teamRepository.findTeamByMember(username).isPresent()) {
            roles.add(new SimpleGrantedAuthority("ROLE_TEAM_MEMBER"));
        } else {
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), roles);
    }
}
