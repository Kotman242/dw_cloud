package com.example.cloud.config;

import com.example.cloud.jwt.JwtAuthEntryPoint;
import com.example.cloud.jwt.JwtRequestFilter;
import com.example.cloud.security.CustomAuthenticationSuccessHandler;
import com.example.cloud.security.CustomUsernamePasswordAuthenticationFilter;
import com.example.cloud.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = false)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    @Value("${cors.mapping}")
    private String mapping;
    @Value("${cors.credential}")
    private boolean credential;
    @Value("${cors.origin}")
    private String origin;
    @Value("${cors.method}")
    private String method;
    private final UserDetailsServiceImpl service;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(mapping)
                .allowCredentials(credential)
                .allowedOrigins(origin)
                .allowedMethods(method);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOrigins("http://localhost:8082")
//                .allowedMethods("*");
//    }


    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(service)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService, PasswordEncoder encoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder);
        return authenticationManagerBuilder.build();
    }

//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.cors();
//        httpSecurity.authorizeHttpRequests().requestMatchers("/h2-console/**").permitAll();
//        httpSecurity.csrf().disable();
//        httpSecurity.headers().frameOptions().disable();
//
//        httpSecurity
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .and()
//                .authorizeHttpRequests().requestMatchers("/login").permitAll();
//
//        httpSecurity
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthEntryPoint)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//
//        return httpSecurity.build();
//    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

