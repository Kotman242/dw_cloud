package com.example.cloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<String,Long> {

}
