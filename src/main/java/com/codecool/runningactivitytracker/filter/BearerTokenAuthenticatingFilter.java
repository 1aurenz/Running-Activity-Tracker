package com.codecool.runningactivitytracker.filter;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BearerTokenAuthenticatingFilter extends OncePerRequestFilter {

    private static final String SERVICE_TOKEN = System.getProperty("user.srv.token", "MY-TOKEN");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer")) {
            // this is not a bearer token based authorization, so we let other filters
            // perform the authentication, if they can:
            filterChain.doFilter(request, response);
            return;
        }
        String expectedHeaderValue = "Bearer " + SERVICE_TOKEN;
        if (!expectedHeaderValue.equals(token)) {
            throw new BadCredentialsException("invalid token found in Authorization header");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                "UserRegistrationApi", "", List.of(new SimpleGrantedAuthority("ROLE_USER_REGISTRATION"))
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
