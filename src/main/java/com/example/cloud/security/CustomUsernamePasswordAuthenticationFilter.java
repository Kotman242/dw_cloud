package com.example.cloud.security;

import com.example.cloud.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String username;
    private String password;

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            System.out.println(password);
            return password;
        } else {
            return super.obtainPassword(request);
        }
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            System.out.println(username);
            return username;
        } else {
            return super.obtainUsername(request);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            try {
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = request.getReader();

                while (reader.ready()) {
                    buffer.append(reader.readLine());
                }

                LoginRequest loginRequest = new ObjectMapper().readValue(buffer.toString(), LoginRequest.class);

                this.username = loginRequest.getLogin();
                this.password = loginRequest.getPassword();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(this.getAuthenticationManager());
    }
}
