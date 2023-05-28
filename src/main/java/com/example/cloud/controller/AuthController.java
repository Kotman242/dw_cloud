package com.example.cloud.controller;

import com.example.cloud.dto.request.LoginRequest;

import com.example.cloud.dto.response.AuthenticationResponse;
import com.example.cloud.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {


    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest authenticationRQ) {
        return authenticationService.login(authenticationRQ);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@RequestHeader("auth-token") String authToken) {
        authenticationService.logout(authToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
