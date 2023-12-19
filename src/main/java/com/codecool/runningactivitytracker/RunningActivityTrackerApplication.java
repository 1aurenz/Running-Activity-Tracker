package com.codecool.runningactivitytracker;

import com.codecool.runningactivitytracker.filter.BearerTokenAuthenticatingFilter;
import com.codecool.runningactivitytracker.repository.TeamRepository;
import com.codecool.runningactivitytracker.repository.UserRepository;
import com.codecool.runningactivitytracker.service.RepositoryBackedUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class RunningActivityTrackerApplication
        extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    public RunningActivityTrackerApplication(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RunningActivityTrackerApplication.class, args);
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http.csrf().disable()
                .httpBasic().and() // with this line we enable HTTP basic auth
                .addFilterAfter(new BearerTokenAuthenticatingFilter(), ExceptionTranslationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/internal/**").hasRole("USER_REGISTRATION")
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder
                .userDetailsService(new RepositoryBackedUserDetailsService(userRepository, teamRepository))
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
