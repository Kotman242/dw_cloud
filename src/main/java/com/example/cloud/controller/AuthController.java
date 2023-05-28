package com.example.cloud.controller;

import com.example.cloud.dto.request.LoginRequest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

//    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
//    void getOptions(@Valid @RequestBody LoginRequest request){
//        System.out.println(request);
//    }

    @PostMapping( "/login")
    void login(@RequestBody LoginRequest request){
        System.out.println(request);
    }
}
