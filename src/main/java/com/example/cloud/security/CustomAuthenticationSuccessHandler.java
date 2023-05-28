package com.example.cloud.security;

import com.example.cloud.dto.request.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            response.getWriter()
                    .print(new ObjectMapper()
                            .writeValueAsString(new AuthResponse()));
            System.out.println("ДА");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
