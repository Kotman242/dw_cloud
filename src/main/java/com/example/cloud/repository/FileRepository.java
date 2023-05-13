package com.example.cloud.repository;

import com.example.cloud.model.File;
import com.example.cloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {

    List<File> getAllByUser(User user);

    File getByName(String name);
}
